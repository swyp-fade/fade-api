package com.fade.vote.repository;

import com.fade.vote.entity.DailyPopularFeedArchiving;
import com.fade.vote.entity.QDailyPopularFeedArchiving;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

public class DailyPopularFeedArchivingRepositoryImpl implements DailyPopularFeedArchivingRepositoryCustom {
    JPAQueryFactory jpaQueryFactory;

    public DailyPopularFeedArchivingRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<DailyPopularFeedArchiving> findMonthlyPopularFeedArchiving(LocalDateTime startOfDate, LocalDateTime endOfDate) {
        QDailyPopularFeedArchiving dailyPopularFeedArchiving = QDailyPopularFeedArchiving.dailyPopularFeedArchiving;

        return jpaQueryFactory
                .selectFrom(dailyPopularFeedArchiving)
                .where(dailyPopularFeedArchiving.archivedAt.between(startOfDate, endOfDate))
                .fetch();
    }
}
