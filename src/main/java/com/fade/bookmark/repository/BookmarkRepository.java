package com.fade.bookmark.repository;

import com.fade.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByMemberIdAndFeedId(Long memberId, Long feedId);
    Boolean existsByMemberIdAndFeedId(Long memberId, Long feedId);
}
