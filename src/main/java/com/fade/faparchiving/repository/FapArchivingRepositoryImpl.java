package com.fade.faparchiving.repository;

import com.fade.faparchiving.entity.FapArchiving;
import com.fade.faparchiving.entity.QFapArchiving;
import com.fade.feed.entity.QFeed;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

public class FapArchivingRepositoryImpl implements FapArchivingRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;

    public FapArchivingRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<FapArchiving> findFapArchivingItems(LocalDateTime startOfDate, LocalDateTime endOfDate) {
        QFapArchiving fapArchivingQ = QFapArchiving.fapArchiving;

        return jpaQueryFactory
                .selectFrom(fapArchivingQ)
                .where(fapArchivingQ.archivedAt.between(startOfDate, endOfDate))
                .fetch();
    }

    @Override
    public Long countDeletedFeedsInFapArchiving() {
        QFeed feedQ = QFeed.feed;
        QFapArchiving fapArchivingQ = QFapArchiving.fapArchiving;

        return jpaQueryFactory
                .select(fapArchivingQ.count())
                .from(fapArchivingQ)
                .join(fapArchivingQ.feed, feedQ)
                .where(feedQ.deletedAt.isNotNull())
                .fetchOne();
    }

    @Override
    public Long countByCondition(Long feedId) {
        QFapArchiving fapArchivingQ = QFapArchiving.fapArchiving;
        return jpaQueryFactory
                .select(fapArchivingQ.count())
                .from(fapArchivingQ)
                .where(fapArchivingQ.feed.id.eq(feedId))
                .fetchOne();
    }

    @Override
    public Long countByMemberId(Long memberId) {
        QFapArchiving fapArchivingQ = QFapArchiving.fapArchiving;
        return jpaQueryFactory
                .select(fapArchivingQ.count())
                .from(fapArchivingQ)
                .where(fapArchivingQ.member.id.eq(memberId))
                .fetchOne();
    }
}
