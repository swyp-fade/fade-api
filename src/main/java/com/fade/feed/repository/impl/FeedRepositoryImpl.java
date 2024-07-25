package com.fade.feed.repository.impl;

import com.fade.feed.dto.request.FindFeedRequest;
import com.fade.feed.entity.Feed;
import com.fade.feed.entity.QFeed;
import com.fade.feed.repository.CustomFeedRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedRepositoryImpl extends QuerydslRepositorySupport implements CustomFeedRepository {
    public FeedRepositoryImpl() {
        super(Feed.class);
    }

    private final QFeed feedQ = QFeed.feed;

    @Override
    public List<Feed> findFeeds(FindFeedRequest findFeedRequest) {
        final var query = super.from(feedQ);

        query.where(
            this.nextCursorLt(findFeedRequest.nextCursor()),
            this.memberIdEq(findFeedRequest.memberId())
        );

        query.limit(findFeedRequest.limit());
        query.orderBy(feedQ.id.desc());

        return query.fetch();
    }

    private BooleanExpression nextCursorLt(Long nextCursor) {
        return nextCursor != null ? feedQ.id.lt(nextCursor) : null;
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? feedQ.member.id.eq(memberId) : null;
    }
}
