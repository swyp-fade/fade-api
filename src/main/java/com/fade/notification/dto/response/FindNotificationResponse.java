package com.fade.notification.dto.response;

import com.fade.notification.constant.NotificationType;

import java.time.LocalDateTime;
import java.util.List;

public record FindNotificationResponse(
        List<FindNotificationItemsResponse> notifications,
        Long nextCursor
) {
    public record FindNotificationItemsResponse(
            Long id,
            Long detailId,
            NotificationType type,
            LocalDateTime createdAt
    ) {
    }
}
