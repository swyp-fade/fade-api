package com.fade.notification.repository;

import com.fade.notification.entity.Notification;
import com.fade.notification.entity.QNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class NotificationRepositoryImpl implements NotificationRepositoryCustom {
    JPAQueryFactory jpaQueryFactory;

    public NotificationRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Notification> findNotificationsUsingNoOffset(Long memberId, Long nextCursor, int limit) {
        QNotification notificationQ = QNotification.notification;

        return jpaQueryFactory
                .selectFrom(notificationQ)
                .where(notificationQ.id.lt(nextCursor), notificationQ.receiver.id.eq(memberId))
                .limit(limit)
                .orderBy(notificationQ.id.desc())
                .fetch();
    }
}
