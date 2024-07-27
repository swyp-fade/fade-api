package com.fade.subscribe.repository;

import com.fade.subscribe.entity.QSubscribe;
import com.fade.subscribe.entity.Subscribe;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SubscribeRepositoryImpl implements SubscribeRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;

    public SubscribeRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Subscribe> findSubscribersUsingNoOffset(Long memberId, Long nextCursor, int limit) {
        final var subscribeQ = QSubscribe.subscribe;
        return jpaQueryFactory
                .selectFrom(subscribeQ)
                .where(subscribeQ.id.lt(nextCursor), subscribeQ.fromMember.id.eq(memberId))
                .limit(limit)
                .orderBy(subscribeQ.id.desc())
                .fetch();
    }

    @Override
    public List<Long> findByFromMemberToMemberIds(Long fromMemberId) {
        final var subscribeQ = QSubscribe.subscribe;

        return jpaQueryFactory
                .select(subscribeQ.toMember.id)
                .from(subscribeQ)
                .fetch();
    }
}
