package com.fade.feed.repository.impl;

import com.fade.feed.dto.request.FindFeedRequest;
import com.fade.feed.entity.Feed;
import com.fade.feed.entity.QFeed;
import com.fade.feed.repository.CustomFeedRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

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

        return query.fetch();
    }
}
