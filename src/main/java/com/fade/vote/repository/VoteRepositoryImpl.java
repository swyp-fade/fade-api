package com.fade.vote.repository;

import com.fade.vote.constant.VoteType;
import com.fade.vote.dto.FindMostVoteItemDto;
import com.fade.vote.dto.response.FindVoteResponse.FindVoteItemResponse;
import com.fade.vote.entity.QVote;
import com.fade.vote.entity.Vote;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class VoteRepositoryImpl implements VoteRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;

    public VoteRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Vote> findVoteUsingNoOffset(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        QVote vote = QVote.vote;
        return jpaQueryFactory
                .selectFrom(vote)
                .where(vote.member.id.eq(memberId), vote.votedAt.between(startDate, endDate))
                .orderBy(vote.id.desc())
                .fetch();
    }

    @Override
    public Vote findOldestVoteByMember(Long memberId) {
        QVote vote = QVote.vote;
        return jpaQueryFactory
                .selectFrom(vote)
                .where(vote.member.id.eq(memberId))
                .orderBy(vote.votedAt.asc())
                .fetchFirst();
    }

    @Override
    public Vote findLatestVoteByMember(Long memberId) {
        QVote vote = QVote.vote;

        return jpaQueryFactory
                .selectFrom(vote)
                .where(vote.member.id.eq(memberId))
                .orderBy(vote.votedAt.desc())
                .fetchFirst();
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
    public Vote findNextUpCursor(LocalDateTime lastUpCursor) {
        QVote vote = QVote.vote;

        return jpaQueryFactory.selectFrom(vote)
                .where(vote.votedAt.gt(lastUpCursor))
                .orderBy(vote.votedAt.desc())
                .fetchFirst();
    }

    @Override
    public Vote findNextDownCursor(LocalDateTime lastDownCursor) {
        QVote vote = QVote.vote;

        return jpaQueryFactory.selectFrom(vote)
                .where(vote.votedAt.lt(lastDownCursor))
                .orderBy(vote.votedAt.desc())
                .fetchFirst();
    }
}
