package com.fade.bon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record CreateBonCommentReq(
        @Positive
        @Schema(minLength = 1, maxLength = 2000)
        @Length(min = 1, max = 2000)
        String content
) {
}
