package com.fade.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindNotificationRequest(
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Long nextCursor,

        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "10")
        Integer limit
) {
    public FindNotificationRequest {
        if (limit == null) {
            limit = 10;
        }
    }
}