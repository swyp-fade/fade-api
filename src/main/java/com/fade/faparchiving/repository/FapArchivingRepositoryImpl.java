package com.fade.faparchiving.repository;

import com.fade.faparchiving.entity.FapArchiving;
import com.fade.faparchiving.entity.QFapArchiving;
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
        QFapArchiving feedArchivingQ = QFapArchiving.fapArchiving;

        return jpaQueryFactory
                .selectFrom(feedArchivingQ)
                .where(feedArchivingQ.archivedAt.between(startOfDate, endOfDate))
                .fetch();
    }
}
