package com.fade.bookmark.service;


import com.fade.bookmark.entitiy.Bookmark;
import com.fade.bookmark.exception.BookmarkNotFoundException;
import com.fade.bookmark.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    public List<Bookmark> getAllBookmarks() {
        return bookmarkRepository.findAll();
    }

    public Bookmark getBookmarkById(Long id) {
        return bookmarkRepository.findById(id).orElseThrow(() -> new BookmarkNotFoundException(id));
    }

    public Bookmark createBookmark(Bookmark bookmark) {
        return bookmarkRepository.save(bookmark);
    }

    public Bookmark updateBookmark(Long id, Bookmark bookmarkDetails) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new BookmarkNotFoundException(id));

        bookmark.setTitle(bookmarkDetails.getTitle());
        bookmark.setUrl(bookmarkDetails.getUrl());
        bookmark.setDescription(bookmarkDetails.getDescription());

        return bookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(Long id) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new BookmarkNotFoundException(id));
        bookmarkRepository.delete(bookmark);
    }
}