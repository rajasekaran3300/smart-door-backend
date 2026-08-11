package com.smartdoor.security.service.impl;

import com.smartdoor.security.dto.response.NotificationResponse;
import com.smartdoor.security.entity.DoorEvent;
import com.smartdoor.security.entity.Notification;
import com.smartdoor.security.entity.User;
import com.smartdoor.security.exception.ResourceNotFoundException;
import com.smartdoor.security.mapper.NotificationMapper;
import com.smartdoor.security.repository.NotificationRepository;
import com.smartdoor.security.repository.UserRepository;
import com.smartdoor.security.service.NotificationService;
import com.smartdoor.security.utility.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public void createOpenDoorNotification(User user, DoorEvent event) {
        String date = DateTimeUtil.formatDate(event.getEventTime());
        String time = DateTimeUtil.formatTime(event.getEventTime());

        Notification notification = Notification.builder()
                .user(user)
                .doorEvent(event)
                .title("Door Open")
                .message(date + " - " + time)
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        // Hook point for future channels — Firebase push / email / browser push all plug in here
        // without touching DoorEventService, since this is the single place OPEN notifications originate.
    }

    @Override
    public List<NotificationResponse> getUnread(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, String username) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!notification.getUser().getUsername().equals(username)) {
            throw new ResourceNotFoundException("Notification not found");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
}
