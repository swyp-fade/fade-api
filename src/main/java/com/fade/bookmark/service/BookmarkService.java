package com.fade.bookmark.service;


import com.fade.bookmark.entity.Bookmark;
import com.fade.bookmark.exception.BookmarkNotFoundException;
import com.fade.bookmark.repository.BookmarkRepository;
import com.fade.bookmark.repository.FeedRepository;
import com.fade.feed.entity.Feed;
import com.fade.member.entity.Member;
import com.fade.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FeedRepository feedRepository;

    public List<Bookmark> getAllBookmarks() {
        return bookmarkRepository.findAll();
    }

    public Bookmark getBookmarkById(Long id) {
        return bookmarkRepository.findById(id).orElseThrow(() -> new BookmarkNotFoundException(id));
    }

    public Bookmark createBookmark(Bookmark bookmark, Long memberId, Long feedId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new RuntimeException("Feed not found"));

        bookmark.setMember(member);
        bookmark.setFeed(feed);

        return bookmarkRepository.save(bookmark);
    }

    public Bookmark updateBookmark(Long id, Bookmark bookmarkDetails) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new BookmarkNotFoundException(id));

        bookmark.setTitle(bookmarkDetails.getTitle());
        bookmark.setUrl(bookmarkDetails.getUrl());
        bookmark.setDescription(bookmarkDetails.getDescription());

        if (bookmarkDetails.getMember() != null) {
            bookmark.setMember(bookmarkDetails.getMember());
        }

        if (bookmarkDetails.getFeed() != null) {
            bookmark.setFeed(bookmarkDetails.getFeed());
        }

        return bookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(Long id) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new BookmarkNotFoundException(id));
        bookmarkRepository.delete(bookmark);
    }
}