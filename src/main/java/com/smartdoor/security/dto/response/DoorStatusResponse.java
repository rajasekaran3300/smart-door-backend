package com.smartdoor.security.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DoorStatusResponse {
    private String houseName;
    private String currentStatus;          // "OPEN" or "CLOSED"
    private LocalDateTime lastOpenedAt;
    private LocalDateTime lastClosedAt;
    private Long todayOpenCount;
    private Long todayCloseCount;
}
