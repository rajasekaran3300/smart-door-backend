package com.smartdoor.security.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.smartdoor.security.utility.DateTimeUtil;

@Entity
@Table(name = "notification")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "door_event_id", nullable = false)
    private DoorEvent doorEvent;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 255)
    private String message;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

   @PrePersist
protected void onCreate() {
    createdAt = DateTimeUtil.now();
}
}
