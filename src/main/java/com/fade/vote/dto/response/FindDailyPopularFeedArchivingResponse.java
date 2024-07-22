package com.fade.vote.dto.response;

public record FindDailyPopularFeedArchivingResponse(
        Long feedId,
        Long memberId,
        String imageUrl
) {
}
