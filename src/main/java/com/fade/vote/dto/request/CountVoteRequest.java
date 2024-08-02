package com.fade.vote.dto.request;

import com.fade.vote.constant.VoteType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
@Builder
public class CountVoteRequest {
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Nullable
    private Long feedId;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Nullable
    private VoteType voteType;
}
