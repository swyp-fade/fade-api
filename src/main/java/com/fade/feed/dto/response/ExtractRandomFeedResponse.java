package com.fade.feed.dto.response;

import com.fade.category.dto.response.FindCategoryListResponse;

import java.util.List;

public record ExtractRandomFeedResponse(
        List<ExtractRandomFeedItemResponse> feeds
) {
    public record ExtractRandomFeedItemResponse(
            Long id,
            String imageURL,
            List<ExtractRandomFeedStyleResponse> styleIds,
            List<ExtractRandomFeedOutfitResponse> outFits,
            Long memberId,
            Boolean isSubscribed,
            Boolean isBookmarked
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
            FindCategoryListResponse.FindCategoryItemResponse category
    ) {
    }
}
