package com.fade.vote.dto.response;

import com.fade.vote.constant.VoteType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FindVoteResponse(
        List<FindVoteItemResponse> voteResultItems,
        LocalDate nextCursorToUpScroll,
        LocalDate nextCursorToDownScroll,
        String direction,
        boolean isLastCursorToUpScroll,
        boolean isLastCursorToDownScroll
) {
    public record FindVoteItemResponse(
            Long voteItemId,
            Long feedId,
            LocalDateTime votedAt,
            VoteType voteType
    ) {
    }
}
