package com.fade.bookmark.dto.request;

public record FindBookmarkRequest(
        Long nextCursor,
        Integer limit
) {
    public FindBookmarkRequest {
        if (limit == null) {
            limit = 10;
        }
    }
}
