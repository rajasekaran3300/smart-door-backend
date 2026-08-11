package com.smartdoor.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Payload the ESP32 sends on every door state change.
 * deviceCode + deviceSecret authenticate the device (not JWT — IoT devices use a shared secret instead).
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DoorEventRequest {

    @NotBlank(message = "Device code is required")
    private String deviceCode;

    @NotBlank(message = "Device secret is required")
    private String deviceSecret;

    @NotNull(message = "Status is required")
    private String status; // "OPEN" or "CLOSED"

    // Optional — if omitted, server time is used
    private LocalDateTime eventTime;
}
