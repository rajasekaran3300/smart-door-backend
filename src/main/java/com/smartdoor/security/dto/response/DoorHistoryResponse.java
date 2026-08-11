package com.smartdoor.security.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DoorHistoryResponse {
    private Long id;
    private String status;
    private LocalDateTime eventTime;
}
