package com.fade.subscribe.repository;

import com.fade.subscribe.dto.request.CountSubscriberRequest;
import com.fade.subscribe.entity.QSubscribe;
import com.fade.subscribe.entity.Subscribe;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SubscribeRepositoryImpl implements SubscribeRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;
    private final QSubscribe subscribeQ = QSubscribe.subscribe;

    public SubscribeRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Subscribe> findSubscribersUsingNoOffset(Long memberId, Long nextCursor, int limit) {
        return jpaQueryFactory
                .selectFrom(subscribeQ)
                .where(this.nextCursorLoe(nextCursor), this.fromMemberIdEq(memberId))
                .limit(limit)
                .orderBy(subscribeQ.id.desc())
                .fetch();
    }

    @Override
    public Subscribe findNextCursor(Long memberId, Long lastCursor) {
        Long lastId = jpaQueryFactory
                .select(subscribeQ.id.min())
                .from(subscribeQ)
                .where(this.fromMemberIdEq(memberId))
                .fetchOne();

        if (lastCursor.equals(lastId)) {
            return null;
        }

        return jpaQueryFactory
                .selectFrom(subscribeQ)
                .where(nextCursorLt(lastCursor), this.fromMemberIdEq(memberId))
                .orderBy(subscribeQ.id.desc())
                .fetchFirst();
    }

    @Override
    public List<Long> findByFromMemberToMemberIds(@NotNull Long fromMemberId) {
        final var subscribeQ = QSubscribe.subscribe;

        return jpaQueryFactory
                .select(subscribeQ.toMember.id)
                .from(subscribeQ)
                .where(fromMemberIdEq(fromMemberId))
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

    private BooleanExpression nextCursorLoe(Long nextCursor) {
        return nextCursor != null ? subscribeQ.id.loe(nextCursor) : null;
    }

    private BooleanExpression nextCursorLt(Long nextCursor) {
        return nextCursor != null ? subscribeQ.id.lt(nextCursor) : null;
    }

    private BooleanExpression toMemberIdEq(Long toMemberId) {
        return toMemberId != null ? subscribeQ.toMember.id.eq(toMemberId) : null;
    }

    private BooleanExpression fromMemberIdEq(Long fromMemberId) {
        return fromMemberId != null ? subscribeQ.fromMember.id.eq(fromMemberId) : null;
    }
}
