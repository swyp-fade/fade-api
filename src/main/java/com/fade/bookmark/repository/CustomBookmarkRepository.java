package com.fade.bookmark.repository;

import com.fade.bookmark.dto.request.BookmarkCountRequest;

public interface CustomBookmarkRepository {
    Long countByCondition(BookmarkCountRequest bookmarkCountRequest);
}
