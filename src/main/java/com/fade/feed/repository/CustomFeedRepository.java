package com.fade.feed.repository;

import com.fade.feed.dto.request.FindFeedRequest;
import com.fade.feed.entity.Feed;

import java.util.List;

public interface CustomFeedRepository {
    List<Feed> findFeeds(FindFeedRequest findFeedRequest);
    List<Feed> findFeeds(FindFeedRequest findFeedRequest, Long targetMemberId);
    List<Feed> extractRandomFeeds(Long memberId);
}
