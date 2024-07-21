package com.fade.feed.dto.response;

import com.fade.category.dto.response.FindCategoryListResponse;

import java.util.List;

public record FindFeedResponse(
        List<FindFeedItemResponse> feeds,
        Long nextCursor
) {
    public record FindFeedItemResponse(
            Long id,
            List<FindFeedStyleResponse> styles,
            List<FindFeedOutfitResponse> feedOutfitList,
            Long memberId
    ) {
    }

    public record FindFeedStyleResponse(
            Integer id,
            String name
    ) {
    }

    public record FindFeedOutfitResponse(
            Long id,
            String brandName,
            String productName,
            FindCategoryListResponse.FindCategoryItemResponse category
    ) {
    }

}
