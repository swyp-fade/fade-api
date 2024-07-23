package com.fade.vote.repository;

import com.fade.vote.entity.DailyPopularFeedArchiving;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyPopularFeedArchivingRepositoryCustom {
    List<DailyPopularFeedArchiving> findMonthlyPopularFeedArchiving(LocalDateTime startOfDate, LocalDateTime endOfDate);
}
