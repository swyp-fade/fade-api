package com.fade.vote.repository;


import com.fade.vote.dto.response.VoteResultResponse.VoteResultItemResponse;
import com.fade.vote.entity.QVote;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class VoteRepositoryImpl implements VoteRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<VoteResultItemResponse> getVoteResultUsingNoOffset(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        QVote vote = QVote.vote;
        return jpaQueryFactory.select(Projections.fields(VoteResultItemResponse.class,
                        vote.id,
                        vote.feed.id,
                        vote.votedAt,
                        vote.voteType))
                .from(vote)
                .where(vote.member.id.eq(memberId),vote.votedAt.between(startDate,endDate))
                .orderBy(vote.id.desc(), vote.votedAt.desc())
                .fetch();
    }
}
