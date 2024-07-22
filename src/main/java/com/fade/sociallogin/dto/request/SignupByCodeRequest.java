package com.fade.sociallogin.dto.request;

import com.fade.global.constant.GenderType;
import com.fade.member.constant.MemberRegexp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record SignupByCodeRequest(
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "소셜 로그인 OAuth 서버에서 전달받은 accessToken값")
        String socialAccessToken,
        @NotNull
        @Schema(
                requiredMode = Schema.RequiredMode.REQUIRED,
                description = "유저id",
                minLength = 4,
                maxLength = 15,
                pattern = MemberRegexp.USERNAME_REGEXP
        )
        @Pattern(regexp = MemberRegexp.USERNAME_REGEXP)
        @Length(min = 4, max = 15)
        String username,
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "성별")
        GenderType genderType
) {
}
