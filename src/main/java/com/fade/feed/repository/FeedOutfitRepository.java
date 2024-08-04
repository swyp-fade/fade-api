package com.fade.feed.repository;

import com.fade.feed.entity.FeedOutfit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedOutfitRepository extends JpaRepository<FeedOutfit, Long> {
    Optional<FeedOutfit> findByFeedId(Long feedId);
}
