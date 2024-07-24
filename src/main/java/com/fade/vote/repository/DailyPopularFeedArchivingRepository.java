package com.fade.vote.repository;

import com.fade.vote.entity.DailyPopularFeedArchiving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyPopularFeedArchivingRepository extends JpaRepository<DailyPopularFeedArchiving, Long>, DailyPopularFeedArchivingRepositoryCustom {
    boolean existsByFeedId(Long feedId);
}
