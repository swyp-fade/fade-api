package com.fade.feed.dto.response;

import com.fade.category.dto.response.FindCategoryListResponse;

import java.util.List;

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
            String username
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
            FindCategoryListResponse.FindCategoryItemResponse categoryIds
    ) {
    }

}
