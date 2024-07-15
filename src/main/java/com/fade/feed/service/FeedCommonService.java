package com.fade.feed.service;

import com.fade.feed.entity.Feed;
import com.fade.feed.exception.FeedNotFoundException;
import com.fade.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedCommonService {

    private final FeedRepository feedRepository;

    public Feed findById(Long feedId) {
        return this.feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);
    }
}
