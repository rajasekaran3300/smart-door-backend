package com.smartdoor.security.controller;

import com.smartdoor.security.dto.request.DoorEventRequest;
import com.smartdoor.security.dto.response.DoorHistoryResponse;
import com.smartdoor.security.dto.response.DoorStatusResponse;
import com.smartdoor.security.service.DoorEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/door")
@RequiredArgsConstructor
public class DoorController {

    private final DoorEventService doorEventService;

    /** Called by the ESP32 on every reed-switch state change. No JWT — authenticated via device secret. */
    @PostMapping("/event")
    public ResponseEntity<Void> receiveEvent(@Valid @RequestBody DoorEventRequest request) {
        doorEventService.recordEvent(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/status")
    public ResponseEntity<DoorStatusResponse> getCurrentStatus(Authentication authentication) {
        return ResponseEntity.ok(doorEventService.getCurrentStatus(authentication.getName()));
    }

    @GetMapping("/history")
    public ResponseEntity<List<DoorHistoryResponse>> getHistory(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(doorEventService.getHistory(authentication.getName(), from, to));
    }

    @GetMapping("/history/today")
    public ResponseEntity<List<DoorHistoryResponse>> getTodayHistory(Authentication authentication) {
        return ResponseEntity.ok(doorEventService.getTodayHistory(authentication.getName()));
    }
}
