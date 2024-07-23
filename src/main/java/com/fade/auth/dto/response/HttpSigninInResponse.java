package com.fade.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record HttpSigninInResponse(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String accessToken
) {
}
