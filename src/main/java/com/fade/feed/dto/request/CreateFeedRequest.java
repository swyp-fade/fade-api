package com.fade.feed.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateFeedRequest(
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        Long attachmentId,
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<DressedUpItem> dressedUpList,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1)
        @NotNull
        @Length(min = 1)
        List<String> styles
) {

        public record DressedUpItem(
                @NotEmpty
                @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
                String categoryCode,
                @NotEmpty
                @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
                String brandName,
                @NotEmpty
                @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
                String productName
        ) {

        }
}
