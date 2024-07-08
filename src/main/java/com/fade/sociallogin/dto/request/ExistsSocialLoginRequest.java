package com.fade.sociallogin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ExistsSocialLoginRequest(
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "소셜 로그인 OAuth 서버에서 전달받은 code값")
        String code
) {

}
