package com.fade.subscribe.dto.response;

import com.fade.category.dto.response.FindCategoryListResponse;

import java.util.List;

public record FindSubscribeFeedResponse(
        List<FindSubscribeFeedItemResponse> subScribeFeeds,
        Long nextCursor
) {
    public record FindSubscribeFeedItemResponse(
            Long id,
            String imageUrl,
            List<FindSubscribeFeedStyleResponse> styles,
            List<FindSubscribeFeedOutfitResponse> subScribeFeedOutfitList,
            Long memberId,
            Boolean isArchiving
    ) {
    }

    public record FindSubscribeFeedStyleResponse(
            Integer id,
            String name
    ) {
    }

    public record FindSubscribeFeedOutfitResponse(
            Long id,
            String brandName,
            String productName,
            FindCategoryListResponse.FindCategoryItemResponse category
    ) {
    }
}
