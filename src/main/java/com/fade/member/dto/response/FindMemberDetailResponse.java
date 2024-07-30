package com.fade.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class FindMemberDetailResponse extends FindMyMemberDetailResponse {
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        private Boolean isSubscribed;
}
