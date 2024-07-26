package com.fade.faparchiving.dto.response;

public record FindFapArchivingResponse(
        Long feedId,
        Long memberId,
        String feedImageUrl
) {
}
