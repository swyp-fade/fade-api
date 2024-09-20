package com.fade.bon.dto.request;

import com.fade.global.constant.SearchType;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindBonCommentRequest(
        @Schema(
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                defaultValue = "ALL"
        )
        SearchType searchType,
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Long nextCursor,
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "10")
        Integer limit
) {
    public FindBonCommentRequest {
        if (searchType == null) {
            searchType = SearchType.ALL;
        }
        if (limit == null) {
            limit = 10;
        }
    }
}
