package com.fade.bon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateBonCommentLikeRes(
        @Schema
        Long commentId
) {
}
