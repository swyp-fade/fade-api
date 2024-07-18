package com.fade.bookmark.service;

import com.fade.bookmark.entity.Bookmark;
import com.fade.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkCommonService {
    private final BookmarkRepository bookmarkRepository;

    public Bookmark findByMemberIdAndFeedId(Long memberId, Long feedId) {
        return bookmarkRepository.findByMemberIdAndFeedId(memberId, feedId)
                .orElseThrow(() -> new RuntimeException("Not Found Bookmark"));
    }
}
