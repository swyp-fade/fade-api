package com.fade.vote.repository;

import com.fade.vote.constant.VoteType;
import com.fade.vote.dto.FindMostVoteItemDto;
import com.fade.vote.dto.request.CountVoteRequest;
import com.fade.vote.entity.QVote;
import com.fade.vote.entity.Vote;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class VoteRepositoryImpl implements VoteRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;
    private final QVote voteQ = QVote.vote;

    public VoteRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Vote> findVoteUsingNoOffset(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        QVote vote = QVote.vote;
        return jpaQueryFactory
                .selectFrom(vote)
                .where(vote.member.id.eq(memberId), vote.votedAt.between(startDate, endDate), vote.feed.deletedAt.isNull())
                .orderBy(vote.id.desc())
                .fetch();
    }

    @Override
    public FindMostVoteItemDto findMostVoteItem() {
        QVote vote = QVote.vote;

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.atTime(LocalTime.MAX);

        return jpaQueryFactory
                .select(Projections.constructor(FindMostVoteItemDto.class,
                        vote.feed.id,
                        vote.feed.member.id))
                .from(vote)
                .where(vote.voteType.eq(VoteType.FADE_IN)
                        .and(vote.votedAt.between(startOfDay, endOfDay)))
                .groupBy(vote.feed.id)
                .orderBy(vote.count().desc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public Vote findNextUpCursor(LocalDateTime lastUpCursor, Long memberId) {
        QVote vote = QVote.vote;

        return jpaQueryFactory.selectFrom(vote)
                .where(vote.votedAt.gt(lastUpCursor), vote.member.id.eq(memberId))
                .orderBy(vote.votedAt.desc())
                .fetchFirst();
    }

    @Override
    public Vote findNextDownCursor(LocalDateTime lastDownCursor, Long memberId) {
        QVote vote = QVote.vote;

        return jpaQueryFactory.selectFrom(vote)
                .where(vote.votedAt.lt(lastDownCursor), vote.member.id.eq(memberId))
                .orderBy(vote.votedAt.desc())
                .fetchFirst();
    }

    @Override
    public Long countByCondition(CountVoteRequest countVoteRequest) {
        return jpaQueryFactory
                .select(voteQ.count())
                .from(voteQ)
                .where(
                        this.feedIdEq(countVoteRequest.getFeedId()),
                        this.voteTypeEq(countVoteRequest.getVoteType()))
                .fetchOne();
    }

    private BooleanExpression feedIdEq(Long feedId) {
        return feedId != null ? voteQ.feed.id.eq(feedId) : null;
    }

    private BooleanExpression voteTypeEq(VoteType voteType) {
        return voteType != null ? voteQ.voteType.eq(voteType) : null;
    }
}
