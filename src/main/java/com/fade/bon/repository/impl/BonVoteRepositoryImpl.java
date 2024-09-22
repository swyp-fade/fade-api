package com.fade.bon.repository.impl;

import com.fade.bon.constant.BonVoteType;
import com.fade.bon.dto.request.VoteCountRequest;
import com.fade.bon.entity.BonVote;
import com.fade.bon.entity.QBonVote;
import com.fade.bon.repository.CustomBonVoteRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class BonVoteRepositoryImpl extends QuerydslRepositorySupport implements CustomBonVoteRepository {

    BonVoteRepositoryImpl() {
        super(BonVote.class);
    }

    private final QBonVote bonVoteQ = QBonVote.bonVote;

    @Override
    public Long countByCondition(VoteCountRequest voteCountRequest) {
        final var query = super.from(bonVoteQ);

        query.where(this.bonIdEq(voteCountRequest.getBonId()));

        return query.fetchCount();
    }

    @Override
    public Long countByCondition(VoteCountRequest voteCountRequest, BonVoteType bonVoteType) {
        final var query = super.from(bonVoteQ);

        query.where(this.bonIdEq(voteCountRequest.getBonId()));

        if (bonVoteType == BonVoteType.YES) {
            query.where(bonVoteQ.bonVoteType.eq(BonVoteType.YES));
            return query.fetchCount();
        }

        query.where(bonVoteQ.bonVoteType.eq(BonVoteType.NO));
        return query.fetchCount();
    }

    private BooleanExpression bonIdEq(Long bonId) {
        return bonId != null ? bonVoteQ.bon.id.eq(bonId) : null;
    }
}
