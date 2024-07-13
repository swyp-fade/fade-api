package com.fade.bookmark.repository;

import com.fade.bookmark.entitiy.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}