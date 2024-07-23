package com.fade.feed.repository.impl;

import com.fade.feed.dto.request.FindFeedRequest;
import com.fade.feed.entity.Feed;
import com.fade.feed.entity.QFeed;
import com.fade.feed.repository.CustomFeedRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedRepositoryImpl extends QuerydslRepositorySupport implements CustomFeedRepository {
    public FeedRepositoryImpl() {
        super(Feed.class);
    }

    @Override
    public List<Feed> findFeeds(FindFeedRequest findFeedRequest) {
        final var feedQ = QFeed.feed;
        final var query = super.from(feedQ);

        if (findFeedRequest.memberId() != null) {
            query.where(feedQ.member.id.eq(findFeedRequest.memberId()));
        }

        if (findFeedRequest.nextCursor() != null) {
            query.where(feedQ.id.lt(findFeedRequest.nextCursor()));
        }

        query.limit(findFeedRequest.limit());
        query.orderBy(feedQ.id.desc());

        return query.fetch();
    }

    @Override
    public List<Feed> findFeedsByMemberIds(List<Long> memberIds, Long nextCursor, int limit) {
        final var feedQ = QFeed.feed;
        final var query = super.from(feedQ);

        if (nextCursor != null) {
            query.where(feedQ.id.lt(nextCursor)
                    .and(feedQ.member.id.in(memberIds)));
        } else {
            query.where(feedQ.member.id.in(memberIds));
        }

        return query
                .orderBy(feedQ.id.desc())
                .limit(limit)
                .fetch();
    }
}
