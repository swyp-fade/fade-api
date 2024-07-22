package com.fade.vote.repository;

import com.fade.vote.entity.DailyPopularFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyPopularFeedRepository extends JpaRepository<DailyPopularFeed, Long> {
}
