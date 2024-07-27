package com.fade.feed.dto.response;

import com.fade.category.dto.response.FindCategoryListResponse;

import java.util.List;

public record FindFeedResponse(
        List<FindFeedItemResponse> feeds,
        Long nextCursor
) {
    public record FindFeedItemResponse(
            Long id,
            String imageUrl,
            List<Integer> styleIds,
            List<FindFeedOutfitResponse> outfits,
            Long memberId,
            Boolean isFAPFeed,
            Boolean isSubscribed,
            Boolean isBookmarked,
            Boolean isMine
    ) {
    }

    public record FindFeedOutfitResponse(
            Long id,
            String brandName,
            String detail,
            FindCategoryListResponse.FindCategoryItemResponse category
    ) {
    }

}
