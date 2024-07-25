package com.fade.feed.dto.request;

import com.fade.global.constant.GenderType;
import com.fade.global.constant.SortType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.List;

public record FindFeedRequest(
            @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            Long memberId,
            @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            List<Long> memberIds,
            @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "10")
            Integer limit,
            @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            Long nextCursor,
            @Schema(
                    requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                    description = "SUBSCRIBE = 구독한 사람의 피드만," +
                            "BOOKMARK = 북마크한 피드만," +
                            "빈배열 = 전체"
            )
            List<FetchType> fetchTypes,
            @Schema(
                    requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                    defaultValue = "DESC"
            )
            SortType sortType,
            @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            List<Integer> styleIds,
            @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            GenderType genderType
) {
    public enum FetchType {
        SUBSCRIBE,
        BOOKMARK
    }

    public FindFeedRequest {
        if (limit == null) {
            limit = 10;
        }

        if (sortType == null) {
            sortType = SortType.DESC;
        }

        if (memberIds == null) {
            memberIds = Collections.emptyList();
        }

        if (fetchTypes == null) {
            fetchTypes = Collections.emptyList();
        }

        if (styleIds == null) {
            styleIds = Collections.emptyList();
        }
    }
}
