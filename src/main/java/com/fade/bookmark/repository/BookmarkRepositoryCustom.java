package com.fade.bookmark.repository;

import com.fade.bookmark.entity.Bookmark;

import java.util.List;

public interface BookmarkRepositoryCustom {
    List<Bookmark> findBookmarksUsingNoOffset(Long memberId, Long nextCursor, int limit);
}
