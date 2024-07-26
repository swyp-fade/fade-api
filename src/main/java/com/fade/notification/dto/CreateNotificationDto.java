package com.fade.notification.dto;

import com.fade.notification.constant.NotificationType;

public record CreateNotificationDto(
        Long receiverId,
        NotificationType type
) {
}
