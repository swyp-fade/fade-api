package com.fade.feed.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FindFeedRequest(
            @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
            Long memberId,
            @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "10")
            Integer limit,
            @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            Long nextCursor
) {
    public FindFeedRequest {
        if (limit == null) {
            limit = 10;
        }
    }
}
