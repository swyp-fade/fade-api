package com.fade.feed.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ExtractRandomFeedResponse(
        List<ExtractRandomFeedItemResponse> feeds
) {
    public record ExtractRandomFeedItemResponse(
            Long id,
            String imageURL,
            List<ExtractRandomFeedStyleResponse> styleIds,
            List<ExtractRandomFeedOutfitResponse> outfits,
            Long memberId,
            Boolean isSubscribed,
            Boolean isBookmarked,
            LocalDateTime createdAt
    ) {
    }

    public record ExtractRandomFeedStyleResponse(
            Integer id
    ) {
    }

    public record ExtractRandomFeedOutfitResponse(
            Long id,
            String brandName,
            String details,
            Integer categoryId
    ) {
    }
}
