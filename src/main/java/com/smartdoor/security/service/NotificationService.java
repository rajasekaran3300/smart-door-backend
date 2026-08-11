package com.smartdoor.security.service;

import com.smartdoor.security.dto.response.NotificationResponse;
import com.smartdoor.security.entity.DoorEvent;
import com.smartdoor.security.entity.User;

import java.util.List;

public interface NotificationService {

    /** Only ever called for OPEN events — enforced by the caller (DoorEventServiceImpl). */
    void createOpenDoorNotification(User user, DoorEvent event);

    List<NotificationResponse> getUnread(String username);

    void markAsRead(Long notificationId, String username);
}
