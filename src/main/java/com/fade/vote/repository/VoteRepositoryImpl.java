package com.fade.vote.repository;


import com.fade.vote.dto.response.FindVoteResponse;
import com.fade.vote.dto.response.FindVoteResponse.FindVoteItemResponse;
import com.fade.vote.entity.QVote;
import com.fade.vote.entity.Vote;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
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
}
