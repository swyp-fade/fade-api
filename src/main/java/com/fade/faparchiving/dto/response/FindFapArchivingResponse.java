package com.fade.faparchiving.dto.response;

import com.fade.category.dto.response.FindCategoryListResponse;

import java.util.List;

public record FindFapArchivingResponse(
        List<FindFapArchivingItemResponse> feeds
) {

    public record FindFapArchivingItemResponse(
            Long feedId,
            String feedImageUrl,
            List<FindFapArchivingStyleResponse> styles,
            List<FindFapArchivingOutfitResponse> outfits,
            Long memberId,
            Boolean isSubscribed,
            Boolean isMine
    ) {
    }

    public record FindFapArchivingStyleResponse(
            Integer id
    ) {
    }

    public record FindFapArchivingOutfitResponse(
        Long id,
        String brandName,
        String details,
        FindCategoryListResponse.FindCategoryItemResponse category
    ) {
    }
}
