package com.fade.bookmark.exception;


public class BookmarkNotFoundException extends RuntimeException {
    public BookmarkNotFoundException(Long id) {
        super("Could not find bookmark with id " + id);
    }
}