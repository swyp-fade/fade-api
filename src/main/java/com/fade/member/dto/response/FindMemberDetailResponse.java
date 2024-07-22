package com.fade.member.dto.response;

import com.fade.global.constant.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record FindMemberDetailResponse (
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        GenderType genderType,
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String username,
        @Nullable
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String profileImageUrl
) {
}
