package com.smartdoor.security.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.smartdoor.security.utility.DateTimeUtil;

@Entity
@Table(name = "door_event")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DoorEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private DoorDevice device;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private DoorStatus status;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = DateTimeUtil.now();
        if (eventTime == null) {
            eventTime = DateTimeUtil.now();
        }
    }

    public enum DoorStatus {
        OPEN, CLOSED
    }
}
