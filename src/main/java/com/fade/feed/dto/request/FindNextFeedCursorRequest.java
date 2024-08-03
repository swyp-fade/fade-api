package com.fade.feed.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.util.List;

@Builder
public record FindNextFeedCursorRequest(
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Nullable
        Long lastCursor,

        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<FindFeedRequest.FetchType> fetchTypes,

        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Long targetMemberId
) {
}
