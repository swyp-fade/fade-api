package com.fade.vote.repository;

import com.fade.vote.constant.VoteType;
import com.fade.vote.dto.FindDailyPopularFeedDto;
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
    public List<FindVoteItemResponse> findVoteUsingNoOffset(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        QVote vote = QVote.vote;
        return jpaQueryFactory.select(Projections.constructor(FindVoteItemResponse.class,
                        vote.id,
                        vote.feed.id,
                        vote.votedAt,
                        vote.voteType))
                .from(vote)
                .where(vote.member.id.eq(memberId), vote.votedAt.between(startDate, endDate))
                .orderBy(vote.id.desc())
                .fetch();
    }

    @Override
    public Optional<Vote> findOldestVoteByMember(Long memberId) {
        QVote vote = QVote.vote;
        Vote result = jpaQueryFactory
                .selectFrom(vote)
                .where(vote.member.id.eq(memberId))
                .orderBy(vote.votedAt.asc())
                .fetchFirst();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Vote> findLatestVoteByMember(Long memberId) {
        QVote vote = QVote.vote;

        Vote result = jpaQueryFactory
                .selectFrom(vote)
                .where(vote.member.id.eq(memberId))
                .orderBy(vote.votedAt.desc())
                .fetchFirst();

        return Optional.ofNullable(result);
    }

    @Override
    public FindDailyPopularFeedDto findDailyPopularFeed() {
        QVote vote = QVote.vote;

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.atTime(LocalTime.MAX);

        return jpaQueryFactory.select(Projections.constructor(FindDailyPopularFeedDto.class,
                        vote.feed.id,
                        vote.feed.member.id))
                .where(vote.voteType.eq(VoteType.FADE_IN)
                        .and(vote.votedAt.between(startOfDay, endOfDay)))
                .groupBy(vote.feed.id)
                .orderBy(vote.count().desc())
                .limit(1)
                .fetchOne();
    }
}
