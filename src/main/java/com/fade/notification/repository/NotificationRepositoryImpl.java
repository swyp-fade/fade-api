package com.fade.notification.repository;

import com.fade.notification.entity.Notification;
import com.fade.notification.entity.QNotification;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class NotificationRepositoryImpl implements NotificationRepositoryCustom {
    JPAQueryFactory jpaQueryFactory;

    private final QNotification notificationQ = QNotification.notification;

    public NotificationRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Notification> findNotificationsUsingNoOffset(Long memberId, Long nextCursor, int limit) {
        QNotification notificationQ = QNotification.notification;

        return jpaQueryFactory
                .selectFrom(notificationQ)
                .where(this.nextCursorLoe(nextCursor), notificationQ.receiver.id.eq(memberId))
                .limit(limit)
                .orderBy(notificationQ.id.desc())
                .fetch();
    }

    @Override
    public Notification findNextCursor(Long memberId, Long lastCursor) {
        Long lastId = jpaQueryFactory
                .select(notificationQ.id.min())
                .from(notificationQ)
                .where(this.memberIdEq(memberId))
                .fetchOne();

        if (lastCursor.equals(lastId)) {
            return null;
        }

        return jpaQueryFactory
                .selectFrom(notificationQ)
                .where(this.nextCursorLt(lastCursor), this.memberIdEq(memberId))
                .orderBy(notificationQ.id.desc())
                .fetchFirst();
    }

    private BooleanExpression nextCursorLoe(Long nextCursor) {
        return nextCursor != null ? notificationQ.id.loe(nextCursor) : null;
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? notificationQ.receiver.id.eq(memberId) : null;
    }


    private BooleanExpression nextCursorLt(Long nextCursor) {
        return nextCursor != null ? notificationQ.id.lt(nextCursor) : null;
    }
}
