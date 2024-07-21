package com.fade.vote.dto.response;

import com.fade.vote.constant.VoteType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record VoteResultResponse(
        List<VoteResultItemResponse> voteResultItems,
        LocalDate lastCursor
) {
    public record VoteResultItemResponse(
            Long voteItemId,
            Long feedId,
            LocalDateTime votedAt,
            VoteType voteType
    ) {
    }
}
