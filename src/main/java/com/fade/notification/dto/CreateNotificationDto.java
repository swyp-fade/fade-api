package com.fade.notification.dto;

import com.fade.notification.constant.NotificationType;
import lombok.Builder;

@Builder
public record CreateNotificationDto(
        Long receiverId,
        Long feedId,
        NotificationType type
) {
}
