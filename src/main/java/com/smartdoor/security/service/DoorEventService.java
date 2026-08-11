package com.smartdoor.security.service;

import com.smartdoor.security.dto.request.DoorEventRequest;
import com.smartdoor.security.dto.response.DoorHistoryResponse;
import com.smartdoor.security.dto.response.DoorStatusResponse;

import java.time.LocalDate;
import java.util.List;

public interface DoorEventService {

    /** Called by the ESP32. Persists the event and — only if status == OPEN — fires a notification. */
    void recordEvent(DoorEventRequest request);

    DoorStatusResponse getCurrentStatus(String username);

    List<DoorHistoryResponse> getHistory(String username, LocalDate from, LocalDate to);

    List<DoorHistoryResponse> getTodayHistory(String username);
}
