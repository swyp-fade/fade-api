package com.fade.bon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
@Builder
public class VoteCountRequest {
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Nullable
    private Long bonId;
}
