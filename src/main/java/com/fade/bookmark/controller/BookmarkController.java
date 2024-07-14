package com.fade.bookmark.controller;

import com.fade.bookmark.dto.BookmarkDto;
import com.fade.bookmark.dto.BookmarkRequestDto;
import com.fade.bookmark.dto.BookmarkResponseDto;
import com.fade.bookmark.entity.Bookmark;
import com.fade.bookmark.repository.FeedRepository;
import com.fade.bookmark.service.BookmarkService;
import com.fade.feed.entity.Feed;
import com.fade.member.entity.Member;
import com.fade.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @GetMapping
    public List<BookmarkResponseDto> getAllBookmarks() {
        return bookmarkService.getAllBookmarks().stream()
                .map(bookmark -> new BookmarkResponseDto(
                        bookmark.getId(),
                        bookmark.getTitle(),
                        bookmark.getUrl(),
                        bookmark.getDescription(),
                        bookmark.getMember().getId(),
                        bookmark.getFeed().getId()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookmarkResponseDto> getBookmarkById(@PathVariable Long id) {
        Bookmark bookmark = bookmarkService.getBookmarkById(id);
        BookmarkResponseDto response = new BookmarkResponseDto(
                bookmark.getId(),
                bookmark.getTitle(),
                bookmark.getUrl(),
                bookmark.getDescription(),
                bookmark.getMember().getId(),
                bookmark.getFeed().getId()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BookmarkResponseDto> createBookmark(@RequestBody BookmarkRequestDto bookmarkRequestDto) {
        Bookmark bookmark = new Bookmark();
        bookmark.setTitle(bookmarkRequestDto.title());
        bookmark.setUrl(bookmarkRequestDto.url());
        bookmark.setDescription(bookmarkRequestDto.description());

        Bookmark createdBookmark = bookmarkService.createBookmark(bookmark, bookmarkRequestDto.memberId(), bookmarkRequestDto.feedId());
        BookmarkResponseDto response = new BookmarkResponseDto(
                createdBookmark.getId(),
                createdBookmark.getTitle(),
                createdBookmark.getUrl(),
                createdBookmark.getDescription(),
                createdBookmark.getMember().getId(),
                createdBookmark.getFeed().getId()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookmarkResponseDto> updateBookmark(@PathVariable Long id, @RequestBody BookmarkRequestDto bookmarkRequestDto) {
        Bookmark bookmark = new Bookmark();
        bookmark.setTitle(bookmarkRequestDto.title());
        bookmark.setUrl(bookmarkRequestDto.url());
        bookmark.setDescription(bookmarkRequestDto.description());

        Bookmark updatedBookmark = bookmarkService.updateBookmark(id, bookmark);
        BookmarkResponseDto response = new BookmarkResponseDto(
                updatedBookmark.getId(),
                updatedBookmark.getTitle(),
                updatedBookmark.getUrl(),
                updatedBookmark.getDescription(),
                updatedBookmark.getMember().getId(),
                updatedBookmark.getFeed().getId()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id) {
        bookmarkService.deleteBookmark(id);
        return ResponseEntity.noContent().build();
    }
}