package com.fade.feed.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public record FindFeedResponse(
        List<FindFeedItemResponse> feeds,
        Long nextCursor
) {
    public record FindFeedItemResponse(
            Long id,
            String imageURL,
            List<FindFeedStyleResponse> styleIds,
            List<FindFeedOutfitResponse> outfits,
            Long memberId,
            Boolean isFAPFeed,
            Boolean isSubscribed,
            Boolean isBookmarked,
            Boolean isMine,
            Long bookmarkCount,
            String username,
            Long reportCount,
            Long fadeInCount,
            LocalDateTime createdAt
    ) {
    }

    public record FindFeedStyleResponse(
            Integer id
    ) {
    }

    public record FindFeedOutfitResponse(
            Long id,
            String brandName,
            String details,
            Integer categoryId
    ) {
    }

}
