package com.fade.feed.dto.response;

import com.fade.category.dto.response.FindCategoryListResponse;

import java.util.List;

public record ExtractRandomFeedResponse(
        List<ExtractRandomFeedItemResponse> feeds
) {
    public record ExtractRandomFeedItemResponse(
            Long id,
            String imageUrl,
            List<ExtractRandomFeedStyleResponse> styles,
            List<ExtractRandomFeedOutfitResponse> feedOutfitList,
            Long memberId
    ) {
    }

    public record ExtractRandomFeedStyleResponse(
            Integer id,
            String name
    ) {
    }

    public record ExtractRandomFeedOutfitResponse(
            Long id,
            String brandName,
            String productName,
            FindCategoryListResponse.FindCategoryItemResponse category
    ) {
    }
}
