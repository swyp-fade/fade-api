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
            Long feedId,
            NotificationType type,
            LocalDateTime createdAt,
            Boolean isRead,
            Long reportCount,
            LocalDateTime selectedDate,
            Long deletedFAPCount
    ) {
    }
}
