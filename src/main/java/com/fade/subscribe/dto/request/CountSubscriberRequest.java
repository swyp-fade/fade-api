package com.fade.subscribe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.Nullable;

public record CountSubscriberRequest(
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Nullable
        Long fromMemberId,
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Nullable
        Long toMemberId
) {
}
