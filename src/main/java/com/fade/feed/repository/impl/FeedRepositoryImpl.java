package com.fade.feed.repository.impl;

import com.fade.bookmark.entity.QBookmark;
import com.fade.feed.dto.request.FindFeedRequest;
import com.fade.feed.dto.request.FindNextFeedCursorRequest;
import com.fade.feed.entity.Feed;
import com.fade.feed.entity.QFeed;
import com.fade.feed.repository.CustomFeedRepository;
import com.fade.global.constant.GenderType;
import com.fade.subscribe.entity.QSubscribe;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.fade.vote.entity.QVote;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Repository
public class FeedRepositoryImpl extends QuerydslRepositorySupport implements CustomFeedRepository {
    public FeedRepositoryImpl() {
        super(Feed.class);
    }

    private final QFeed feedQ = QFeed.feed;
    private final QBookmark bookmarkQ = QBookmark.bookmark;
    private final QSubscribe subscribeQ = QSubscribe.subscribe;

    @Override
    public List<Feed> findFeeds(
            FindFeedRequest findFeedRequest
    ) {
        return this.findFeeds(findFeedRequest, null);
    }

    @Override
    public List<Feed> findFeeds(
            FindFeedRequest findFeedRequest,
            Long targetMemberId
    ) {
        final var query = super.from(feedQ);

        query.where(
                this.memberIdEq(findFeedRequest.memberId()),
                this.memberIdsEq(findFeedRequest.memberIds()),
                this.styleIdsEq(findFeedRequest.selectedStyles()),
                this.genderTypeEq(findFeedRequest.gender()),
                this.nextCursorLoe(findFeedRequest.nextCursor())
        );

        findFeedRequest.fetchTypes().forEach((ft) -> {
            switch (ft) {
                case BOOKMARK:
                    query.join(bookmarkQ).on(
                            bookmarkQ.feed.id.eq(feedQ.id),
                            bookmarkQ.member.id.eq(targetMemberId)
                    );
                    query.orderBy(bookmarkQ.bookmarkedAt.desc());
                    break;
                case SUBSCRIBE:
                    query.join(subscribeQ).on(
                            subscribeQ.toMember.id.eq(feedQ.member.id),
                            subscribeQ.fromMember.id.eq(targetMemberId)
                    );
                    break;
            }
        });

        query.limit(findFeedRequest.limit());

        switch (findFeedRequest.sortType()) {
            case DESC:
                query.orderBy(feedQ.id.desc());
                break;
            case ASC:
                query.orderBy(feedQ.id.asc());
                break;
        }

        return query.fetch();
    }

    @Override
    public Feed findNextCursor(FindNextFeedCursorRequest findNextFeedCursorRequest) {
        Long lastId = null;
        final var queryForLastId = super.from(feedQ);
        final var queryForNextCursor = super.from(feedQ);

        checkFetchTypeAndApplyJoin(queryForLastId, findNextFeedCursorRequest);

        lastId = queryForLastId.select(feedQ.id.min()).fetchOne();

        if (findNextFeedCursorRequest.lastCursor().equals(lastId)) {
            return null;
        }

        checkFetchTypeAndApplyJoin(queryForNextCursor, findNextFeedCursorRequest);

        return queryForNextCursor
                .where(nextCursorLt(findNextFeedCursorRequest.lastCursor()))
                .orderBy(feedQ.id.desc())
                .fetchFirst();
    }

    private BooleanExpression nextCursorLoe(Long nextCursor) {
        return nextCursor != null ? feedQ.id.loe(nextCursor) : null;
    }

    private BooleanExpression nextCursorLt(Long nextCursor) {
        return nextCursor != null ? feedQ.id.lt(nextCursor) : null;
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? feedQ.member.id.eq(memberId) : null;
    }

    private BooleanExpression memberIdsEq(List<Long> memberIds) {
        return memberIds.isEmpty() ? null : feedQ.member.id.in(memberIds);
    }

    private BooleanExpression genderTypeEq(GenderType genderType) {
        return genderType != null ? feedQ.member.genderType.eq(genderType) : null;
    }

    private BooleanExpression styleIdsEq(List<Integer> styleIds) {
        if (styleIds == null || styleIds.isEmpty()) {
            return null;
        }

        return feedQ.styles.any().id.in(styleIds);
    }

    @Override
    public List<Feed> extractRandomFeeds(Long memberId) {
        final var feedQ = QFeed.feed;
        final var voteQ = QVote.vote;

        List<Feed> feeds = from(feedQ)
                .where(feedQ.member.id.ne(memberId), feedQ.id.notIn(
                        from(voteQ)
                                .select(voteQ.feed.id)
                                .where(voteQ.member.id.eq(memberId))
                ))
                .fetch();
        Collections.shuffle(feeds, new Random());

        return feeds.size() > 10 ? feeds.subList(0, 10) : feeds;
    }

    private void checkFetchTypeAndApplyJoin(JPQLQuery<Feed> query, FindNextFeedCursorRequest findNextFeedCursorRequest) {
        findNextFeedCursorRequest.fetchTypes().forEach((ft) -> {
            switch (ft) {
                case BOOKMARK:
                    query.join(bookmarkQ).on(
                            bookmarkQ.feed.id.eq(feedQ.id),
                            bookmarkQ.member.id.eq(findNextFeedCursorRequest.targetMemberId())
                    );

                    break;
                case SUBSCRIBE:
                    query.join(subscribeQ).on(
                            subscribeQ.toMember.id.eq(feedQ.member.id),
                            subscribeQ.fromMember.id.eq(findNextFeedCursorRequest.targetMemberId())
                    );
                    break;
            }
        });
    }
}
