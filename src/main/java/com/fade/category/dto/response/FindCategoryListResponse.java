package com.fade.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record FindCategoryListResponse(
        List<FindCategoryItemResponse> categories
) {

    public record FindCategoryItemResponse(
            @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
            String name,
            @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
            String code
    ) {

    }
}
