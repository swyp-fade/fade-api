package com.fade.feed.service;

import com.fade.feed.entity.Feed;
import com.fade.feed.repository.FeedRepository;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedCommonService {

    private final FeedRepository feedRepository;

    public Feed findById(Long feedId) {
        return this.feedRepository.findById(feedId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_FEED));
    }
}
