package com.fade.bookmark.service;


import com.fade.bookmark.entity.Bookmark;
import com.fade.bookmark.repository.BookmarkRepository;
import com.fade.feed.entity.Feed;
import com.fade.feed.service.FeedCommonService;
import com.fade.member.entity.Member;
import com.fade.member.service.MemberCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final MemberCommonService memberCommonService;
    private final FeedCommonService feedCommonService;
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkCommonService bookMarkCommonService;

    @Transactional
    public Long addBookmark(Long memberId, Long feedId) {
        Member member = memberCommonService.findById(memberId);
        Feed feed = feedCommonService.findById(feedId);

        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .feed(feed)
                .build();

        bookmarkRepository.save(bookmark);
        return bookmark.getId();
    }

    @Transactional
    public void cancelBookmark(Long memberId, Long feedId) {
        Bookmark bookmark = bookMarkCommonService.findByMemberIdAndFeedId(memberId, feedId);

        bookmarkRepository.delete(bookmark);
    }

    public Boolean hasBookmark(Long memberId, Long feedId) {
        return this.bookmarkRepository.existsByMemberIdAndFeedId(memberId, feedId);
    }
}
