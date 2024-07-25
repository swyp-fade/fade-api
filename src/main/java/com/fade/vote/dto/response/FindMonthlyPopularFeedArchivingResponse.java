package com.fade.vote.dto.response;

public record FindMonthlyPopularFeedArchivingResponse(
        Long feedId,
        Long memberId,
        String imageUrl
) {
}
