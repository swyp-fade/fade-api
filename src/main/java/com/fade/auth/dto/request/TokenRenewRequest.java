package com.fade.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

public record TokenRenewRequest(
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Nullable
        @org.jetbrains.annotations.Nullable
        String refreshToken
) {

}
