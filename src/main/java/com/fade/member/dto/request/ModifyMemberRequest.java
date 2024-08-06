package com.fade.member.dto.request;

import com.fade.member.constant.MemberRegexp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ModifyMemberRequest(
        @Nullable
        @Schema(
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                pattern = MemberRegexp.USERNAME_REGEXP,
                minLength = 4,
                maxLength = 15
        )
        @Pattern(regexp = MemberRegexp.USERNAME_REGEXP)
        String username,
        @Nullable
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Long profileImageId,
        @Nullable
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String introduceContent
) {

}
