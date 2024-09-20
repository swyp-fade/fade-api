package com.fade.bon.dto.response;

import com.fade.bon.constant.BonVoteType;

import java.time.LocalDateTime;
import java.util.List;

public record FindBonCommentResponse(
        List<FindBonCommentItemResponse> comments,
        Long nextCursor
) {
    public record FindBonCommentItemResponse(
            Long id,
            String content,
            BonVoteType votedType,
            String anonName,
            Long likeCount,
            Boolean hasLiked,
            Boolean isBestComment,
            Boolean isMine,
            LocalDateTime createdAt
    ) {
    }
}
