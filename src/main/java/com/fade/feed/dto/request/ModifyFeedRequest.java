package com.fade.feed.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ModifyFeedRequest(
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<ModifyFeedRequest.OutfitItem> outfits,
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
