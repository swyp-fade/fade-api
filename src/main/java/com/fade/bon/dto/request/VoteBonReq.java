package com.fade.bon.dto.request;

import com.fade.bon.constant.BonVoteType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record VoteBonReq(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        BonVoteType bonVoteType
) {
}
