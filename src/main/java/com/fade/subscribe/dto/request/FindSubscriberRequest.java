package com.fade.subscribe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindSubscriberRequest(
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Long nextCursor,

        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "10")
        Integer limit
) {
    public FindSubscriberRequest {
        if (limit == null) {
            limit = 10;
        }
    }
}
