package com.fade.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateAccessTokenByRefreshTokenResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String accessToken
) {
}
