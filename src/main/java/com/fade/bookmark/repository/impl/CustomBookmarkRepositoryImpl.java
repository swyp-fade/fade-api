package com.fade.bookmark.repository.impl;

import com.fade.bookmark.dto.request.BookmarkCountRequest;
import com.fade.bookmark.entity.Bookmark;
import com.fade.bookmark.entity.QBookmark;
import com.fade.bookmark.repository.CustomBookmarkRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CustomBookmarkRepositoryImpl extends QuerydslRepositorySupport implements CustomBookmarkRepository {
    public CustomBookmarkRepositoryImpl() {
        super(Bookmark.class);
    }

    private final QBookmark bookmarkQ = QBookmark.bookmark;

    @Override
    public Long countByCondition(BookmarkCountRequest bookmarkCountRequest) {
        final var query = super.from(bookmarkQ);

        query.where(this.feedIdEq(bookmarkCountRequest.getFeedId()));

        return query.fetchCount();
    }

    private BooleanExpression feedIdEq(Long feedId) {
        return feedId != null ? bookmarkQ.feed.id.eq(feedId) : null;
    }
}
