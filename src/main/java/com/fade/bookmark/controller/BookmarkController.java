package com.fade.bookmark.controller;

import com.fade.bookmark.entitiy.Bookmark;
import com.fade.bookmark.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @GetMapping
    public List<Bookmark> getAllBookmarks() {
        return bookmarkService.getAllBookmarks();
    }

    @GetMapping("/{id}")
    public Bookmark getBookmarkById(@PathVariable Long id) {
        return bookmarkService.getBookmarkById(id);
    }

    @PostMapping
    public Bookmark createBookmark(@RequestBody Bookmark bookmark) {
        return bookmarkService.createBookmark(bookmark);
    }

    @PutMapping("/{id}")
    public Bookmark updateBookmark(@PathVariable Long id, @RequestBody Bookmark bookmarkDetails) {
        return bookmarkService.updateBookmark(id, bookmarkDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookmark(@PathVariable Long id) {
        bookmarkService.deleteBookmark(id);
        return ResponseEntity.ok().build();
    }
}