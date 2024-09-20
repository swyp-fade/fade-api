package com.fade.bon.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record FindBonResponse(
        List<FindBonItemResponse> bonList,
        Long nextCursor
) {
    public record FindBonItemResponse(
            Long id,
            String title,
            String imageURL,
            Long voteCount,
            Long commentCount,
            Boolean hasVoted,
            Boolean isHot,
            Boolean isMine,
            LocalDateTime createdAt
    ) {
    }
}
