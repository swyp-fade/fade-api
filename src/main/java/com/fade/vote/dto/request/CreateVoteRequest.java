package com.fade.vote.dto.request;

import com.fade.vote.constant.VoteType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateVoteRequest(

        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1, maxLength = 10)
        @Length(min = 1, max = 10)
        List<CreateVoteItemRequest> voteItems
) {
    public record CreateVoteItemRequest(
            @NotEmpty
            @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
            Long feedId,

            @NotEmpty
            @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
            VoteType voteType
    ) {
    }
}
