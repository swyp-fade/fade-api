package com.fade.subscribe.repository;

import com.fade.subscribe.dto.request.CountSubscriberRequest;
import com.fade.subscribe.entity.QSubscribe;
import com.fade.subscribe.entity.Subscribe;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SubscribeRepositoryImpl implements SubscribeRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;
    private final QSubscribe subscribeQ = QSubscribe.subscribe;

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

    @Override
    public Long countByCondition(CountSubscriberRequest countSubscriberRequest) {
        return jpaQueryFactory
                .select(subscribeQ.count())
                .from(subscribeQ)
                .where(
                        this.toMemberIdEq(countSubscriberRequest.toMemberId()),
                        this.fromMemberIdEq(countSubscriberRequest.fromMemberId())
                )
                .fetchOne();
    }

    private BooleanExpression toMemberIdEq(Long toMemberId) {
        return toMemberId != null ? subscribeQ.toMember.id.eq(toMemberId) : null;
    }

    private BooleanExpression fromMemberIdEq(Long fromMemberId) {
        return fromMemberId != null ? subscribeQ.fromMember.id.eq(fromMemberId) : null;
    }
}
