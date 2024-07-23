package com.fade.feed.repository.impl;

import com.fade.feed.dto.request.FindFeedRequest;
import com.fade.feed.entity.Feed;
import com.fade.feed.entity.QFeed;
import com.fade.feed.repository.CustomFeedRepository;
import com.fade.vote.entity.QVote;
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

    @Override
    public List<Feed> extractRandomFeeds(Long memberId) {
        final var feedQ = QFeed.feed;
        final var voteQ = QVote.vote;

        List<Feed> feeds = from(feedQ)
                .where(feedQ.id.notIn(
                        from(voteQ)
                                .select(voteQ.feed.id)
                                .where(voteQ.member.id.eq(memberId))
                ))
                .fetch();
        Collections.shuffle(feeds, new Random());

        return feeds.size() > 10 ? feeds.subList(0, 10) : feeds;
    }
}
