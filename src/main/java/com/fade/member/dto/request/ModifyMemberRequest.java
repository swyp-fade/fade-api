package com.fade.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

public record ModifyMemberRequest(
        @Nullable
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String username
) {

}
