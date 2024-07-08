package com.fade.sociallogin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ExistsSocialLoginResponse(
        @Schema(
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                description = "존재한다면 true 반환"
        )
        Boolean isExists
) {

}
