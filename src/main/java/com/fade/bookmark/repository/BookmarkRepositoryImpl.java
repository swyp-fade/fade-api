package com.fade.bookmark.repository;

import com.fade.bookmark.entity.Bookmark;
import com.fade.bookmark.entity.QBookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;

    public BookmarkRepositoryImpl(EntityManager em) {
        new JPAQueryFactory(em);
    }

    @Override
    public List<Bookmark> findBookmarksUsingNoOffset(Long memberId, Long nextCursor, int limit) {
        final var bookmarkQ = QBookmark.bookmark;

        return jpaQueryFactory
                .selectFrom(bookmarkQ)
                .where(bookmarkQ.id.lt(nextCursor), bookmarkQ.member.id.eq(memberId))
                .limit(limit)
                .orderBy(bookmarkQ.id.desc())
                .fetch();
    }
}
