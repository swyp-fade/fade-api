package com.fade.feed.dto.response;

import java.util.List;

public record FindFeedDetailResponse(
        Long id,
        String imageURL,
        List<FindFeedResponse.FindFeedStyleResponse> styleIds,
        List<FindFeedResponse.FindFeedOutfitResponse> outfits,
        Long memberId,
        Boolean isFAPFeed,
        Boolean isSubscribed,
        Boolean isBookmarked,
        Boolean isMine,
        Long bookmarkCount,
        String username,
        Long reportCount
) {

}
