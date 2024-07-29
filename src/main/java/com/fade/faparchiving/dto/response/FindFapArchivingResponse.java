package com.fade.faparchiving.dto.response;

import java.time.LocalDateTime;
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
            Long fadeInCount,
            Boolean isSubscribed,
            Boolean isBookmarked,
            Boolean isMine,
            LocalDateTime createdAt
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
        Integer categoryId
    ) {
    }
}
