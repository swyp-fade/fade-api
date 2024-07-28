package com.fade.vote.dto.response;

import com.fade.vote.constant.VoteType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FindVoteResponse(
        List<FindVoteItemResponse> feeds,
        LocalDate nextCursorToUpScroll,
        LocalDate nextCursorToDownScroll,
        String direction,
        boolean isLastCursorToUpScroll,
        boolean isLastCursorToDownScroll
) {

    public record FindVoteItemResponse(
            Long voteItemId,
            LocalDateTime votedAt,
            VoteType voteType,
            Long feedId,
            String feedImageURL,
            Boolean isFAPFeed,
            Boolean isSubscribed,
            Boolean isBookmarked,
            List<FindVoteItemStyleResponse> styleIds,
            List<FindVoteItemOutFitResponse> outfits,
            String username,
            String profileImageURL
    ) {
    }

    public record FindVoteItemStyleResponse(
            Integer id
    ) {
    }

    public record FindVoteItemOutFitResponse(
            Long id,
            String brandName,
            String details,
            Integer categoryId
    ) {
    }
}
