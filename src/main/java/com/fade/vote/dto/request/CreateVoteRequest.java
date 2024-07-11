package com.fade.vote.dto.request;

import com.fade.vote.constant.VoteType;
import com.fade.vote.validation.VoteTypeValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateVoteRequest(

        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1, maxLength = 10)
        @Size(min = 1, max = 10)
        List<@Valid CreateVoteItemRequest> voteItems
) {

    public record CreateVoteItemRequest(
            @NotNull
            @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "투표 대상 피드")
            Long feedId,

            @VoteTypeValidation
            @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "투표 타입(ENUM) - FADE_IN or FADE_OUT")
            VoteType voteType
    ) {
    }
}
