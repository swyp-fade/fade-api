package com.fade.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record CreateAccessTokenByRefreshTokenRequest(
        @NotEmpty
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String refreshToken
) {
}
