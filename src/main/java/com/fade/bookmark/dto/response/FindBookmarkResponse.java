package com.fade.bookmark.dto.response;

import java.util.List;

public record FindBookmarkResponse(
        List<FindBookmarkItemResponse> bookmarks,
        Long nextCursor
) {
    public record FindBookmarkItemResponse(
           Long id,
           Long feedId,
           String imageUrl
    ) {
    }
}
