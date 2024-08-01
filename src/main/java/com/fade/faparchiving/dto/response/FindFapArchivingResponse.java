package com.fade.faparchiving.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record FindFapArchivingResponse(
        List<FindFapArchivingItemResponse> feeds
) {

    public record FindFapArchivingItemResponse(
            Long id,
            String imageURL,
            List<FindFapArchivingStyleResponse> styleIds,
            List<FindFapArchivingOutfitResponse> outfits,
            Long memberId,
            String username,
            String profileImageURL,
            Long fadeInCount,
            Boolean isFAPFeed,
            Boolean isSubscribed,
            Boolean isBookmarked,
            Boolean isMine,
            LocalDateTime createdAt,
            LocalDateTime fapSelectedAt
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
