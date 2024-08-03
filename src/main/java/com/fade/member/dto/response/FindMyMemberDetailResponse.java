package com.fade.member.dto.response;

import com.fade.global.constant.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class FindMyMemberDetailResponse {
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        private Long id;
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        private GenderType genderType;
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        private String username;
        @Nullable
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private String profileImageURL;
        @Nullable
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private Long selectedFAPCount;
        @Nullable
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private Long deletedFAPCount;
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        private Long subscribedCount;
        @Nullable
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private String introduceContent;
}
