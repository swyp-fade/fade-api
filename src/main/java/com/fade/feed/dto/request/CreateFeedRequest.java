package com.fade.feed.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateFeedRequest(
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        Long attachmentId,
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<OutfitItem> outfits,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1)
        @NotNull
        @Size(min = 1)
        List<Integer> styleIds
) {
        public record OutfitItem(
                @NotNull
                @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
                Integer categoryId,
                @NotEmpty
                @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
                String brandName,
                @NotEmpty
                @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
                String details
        ) {

        }
}
