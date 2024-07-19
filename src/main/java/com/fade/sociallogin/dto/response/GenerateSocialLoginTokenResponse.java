package com.fade.sociallogin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record GenerateSocialLoginTokenResponse(
        @Schema(
                requiredMode = Schema.RequiredMode.REQUIRED,
                description = "소셜로그인 accessToken"
        )
        String accessToken
) {

}
