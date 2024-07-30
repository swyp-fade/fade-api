package com.fade.subscribe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.jetbrains.annotations.Nullable;

@Builder
public record CountSubscriberRequest(
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Nullable
        Long fromMemberId,
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Nullable
        Long toMemberId
) {
}
