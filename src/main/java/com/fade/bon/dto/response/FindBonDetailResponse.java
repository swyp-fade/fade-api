package com.fade.bon.dto.response;

import com.fade.bon.constant.BonVoteType;

public record FindBonDetailResponse(
        String title,
        String contents,
        String imageURL,
        Long voteCount,
        Long commentCount,
        BonVoteType myVotedValue,
        BonCount bonCount,
        Boolean hasCommented
) {

    public record BonCount(
            Long yes,
            Long no
    ) {
    }
}
