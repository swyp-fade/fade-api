package com.fade.sociallogin.dto.request;

import com.fade.global.constant.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record SignupByCodeRequest(
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "소셜 로그인 OAuth 서버에서 전달받은 code값")
        String code,
        @NotNull
        @Schema(
                requiredMode = Schema.RequiredMode.REQUIRED,
                description = "유저id",
                minLength = 3,
                maxLength = 30
        )
        @Length(min = 3, max = 30)
        String username,
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "성별")
        GenderType genderType,
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "소셜 로그인에 사용했던 OAuth redirectUri")
        String redirectUri
) {
}
