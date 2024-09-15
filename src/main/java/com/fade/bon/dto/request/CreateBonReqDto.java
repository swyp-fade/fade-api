package com.fade.bon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record CreateBonReqDto (
        @Schema(minLength = 1, maxLength = 30)
        @Length(min = 1, max = 30)
        @NotEmpty
        String title,
        @Schema(minLength = 1, maxLength = 2000)
        @Length(min = 1, max = 2000)
        @NotEmpty
        String contents,
        @Schema()
        @Positive
        Long attachmentId
) {
}
