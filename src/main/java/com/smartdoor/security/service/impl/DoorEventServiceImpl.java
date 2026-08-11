package com.smartdoor.security.service.impl;

import com.smartdoor.security.dto.request.DoorEventRequest;
import com.smartdoor.security.dto.response.DoorHistoryResponse;
import com.smartdoor.security.dto.response.DoorStatusResponse;
import com.smartdoor.security.entity.DoorDevice;
import com.smartdoor.security.entity.DoorEvent;
import com.smartdoor.security.entity.House;
import com.smartdoor.security.entity.User;
import com.smartdoor.security.exception.InvalidDeviceException;
import com.smartdoor.security.exception.ResourceNotFoundException;
import com.smartdoor.security.mapper.DoorEventMapper;
import com.smartdoor.security.repository.DoorDeviceRepository;
import com.smartdoor.security.repository.DoorEventRepository;
import com.smartdoor.security.repository.HouseRepository;
import com.smartdoor.security.repository.UserRepository;
import com.smartdoor.security.service.DoorEventService;
import com.smartdoor.security.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoorEventServiceImpl implements DoorEventService {

    private final DoorDeviceRepository doorDeviceRepository;
    private final DoorEventRepository doorEventRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final NotificationService notificationService;
    private final DoorEventMapper doorEventMapper;

    @Override
    @Transactional
    public void recordEvent(DoorEventRequest request) {
        DoorDevice device = doorDeviceRepository.findByDeviceCode(request.getDeviceCode())
                .orElseThrow(() -> new InvalidDeviceException("Unknown device code"));

        // Device authenticates with a shared secret instead of a JWT — it's a headless ESP32, not a logged-in user.
        if (!device.getDeviceSecret().equals(request.getDeviceSecret())) {
            throw new InvalidDeviceException("Invalid device secret");
        }
        if (!device.getIsActive()) {
            throw new InvalidDeviceException("Device is deactivated");
        }

        DoorEvent.DoorStatus status;
        try {
            status = DoorEvent.DoorStatus.valueOf(request.getStatus().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidDeviceException("Status must be OPEN or CLOSED");
        }

        LocalDateTime eventTime = request.getEventTime() != null ? request.getEventTime() : LocalDateTime.now();

        DoorEvent event = DoorEvent.builder()
                .device(device)
                .status(status)
                .eventTime(eventTime)
                .build();
        event = doorEventRepository.save(event);

        device.setLastSeenAt(LocalDateTime.now());
        doorDeviceRepository.save(device);

        // *** The single rule that matters: notify ONLY on OPEN, never on CLOSE. ***
        if (status == DoorEvent.DoorStatus.OPEN) {
            User owner = device.getHouse().getUser();
            notificationService.createOpenDoorNotification(owner, event);
        }
    }

    @Override
    public DoorStatusResponse getCurrentStatus(String username) {
        DoorDevice device = getPrimaryDeviceForUser(username);

        DoorEvent latest = doorEventRepository.findTopByDeviceIdOrderByEventTimeDesc(device.getId())
                .orElse(null);

        LocalDateTime lastOpened = doorEventRepository
                .findTopByDeviceIdAndStatusOrderByEventTimeDesc(device.getId(), DoorEvent.DoorStatus.OPEN)
                .map(DoorEvent::getEventTime).orElse(null);

        LocalDateTime lastClosed = doorEventRepository
                .findTopByDeviceIdAndStatusOrderByEventTimeDesc(device.getId(), DoorEvent.DoorStatus.CLOSED)
                .map(DoorEvent::getEventTime).orElse(null);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        Long openCount = doorEventRepository.countByDeviceIdAndStatusAndEventTimeBetween(
                device.getId(), DoorEvent.DoorStatus.OPEN, startOfDay, endOfDay);
        Long closeCount = doorEventRepository.countByDeviceIdAndStatusAndEventTimeBetween(
                device.getId(), DoorEvent.DoorStatus.CLOSED, startOfDay, endOfDay);

        House house = device.getHouse();

        return DoorStatusResponse.builder()
                .houseName(house.getHouseName())
                .currentStatus(latest != null ? latest.getStatus().name() : "UNKNOWN")
                .lastOpenedAt(lastOpened)
                .lastClosedAt(lastClosed)
                .todayOpenCount(openCount)
                .todayCloseCount(closeCount)
                .build();
    }

    @Override
    public List<DoorHistoryResponse> getHistory(String username, LocalDate from, LocalDate to) {
        DoorDevice device = getPrimaryDeviceForUser(username);
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);

        return doorEventRepository
                .findByDeviceIdAndEventTimeBetweenOrderByEventTimeDesc(device.getId(), start, end)
                .stream()
                .map(doorEventMapper::toHistoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoorHistoryResponse> getTodayHistory(String username) {
        return getHistory(username, LocalDate.now(), LocalDate.now());
    }

    private DoorDevice getPrimaryDeviceForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        House house = houseRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));

        List<DoorDevice> devices = doorDeviceRepository.findByHouseId(house.getId());
        if (devices.isEmpty()) {
            throw new ResourceNotFoundException("No door device registered for this house");
        }
        return devices.get(0); // Single-door MVP; multi-door support is a documented future enhancement
    }
}
