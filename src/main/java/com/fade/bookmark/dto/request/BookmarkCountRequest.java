package com.fade.bookmark.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Builder
@Getter
public class BookmarkCountRequest {
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Nullable
    private Long feedId;
}
