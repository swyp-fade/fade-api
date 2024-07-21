package com.fade.member.dto.response;

import com.fade.global.constant.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindMemberDetailResponse (
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        GenderType genderType,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String username
) {
}
