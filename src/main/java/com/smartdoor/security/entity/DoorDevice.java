package com.smartdoor.security.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.smartdoor.security.utility.DateTimeUtil;

@Entity
@Table(name = "door_device")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DoorDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @Column(name = "device_code", nullable = false, unique = true, length = 64)
    private String deviceCode;

    @Column(name = "device_name", nullable = false, length = 100)
    @Builder.Default
    private String deviceName = "Front Door";

    @Column(name = "device_secret", nullable = false)
    private String deviceSecret;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = DateTimeUtil.now();
    }
}
