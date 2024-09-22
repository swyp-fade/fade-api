package com.fade.bon.repository.impl;

import com.fade.bon.constant.BonVoteType;
import com.fade.bon.dto.request.FindBonRequest;
import com.fade.bon.entity.Bon;
import com.fade.bon.entity.QBon;
import com.fade.bon.entity.QBonVote;
import com.fade.bon.repository.CustomBonRepository;
import com.fade.global.constant.SearchType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BonRepositoryImpl extends QuerydslRepositorySupport implements CustomBonRepository {

    public BonRepositoryImpl() {
        super(Bon.class);
    }

    private final QBon bonQ = QBon.bon;
    private final QBonVote bonVoteQ = QBonVote.bonVote;

    @Override
    public List<Bon> findBons(Long memberId, FindBonRequest findBonRequest) {
        final var query = super.from(bonQ);

        query.where(
                this.nextCursorLoe(findBonRequest.nextCursor())
        );

        switch (findBonRequest.searchType()) {
            case MY_BON:
                query.where(bonQ.member.id.eq(memberId));
                break;
            case VOTED:
                query.where(bonQ.bonVotes.any().member.id.eq(memberId));
                break;
            case NOT_VOTED:
                query.where(bonQ.bonVotes.any().member.id.eq(memberId).not());
                break;
        }

        query.orderBy(bonQ.id.desc());
        query.limit(findBonRequest.limit());

        return query.fetch();
    }

    @Override
    public List<Bon> findHotBons() {
        final var query = super.from(bonQ);
        query.leftJoin(bonVoteQ).on(bonVoteQ.bon.id.eq(bonQ.id));

        LocalDateTime startOfYesterday = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime endOfYesterday = startOfYesterday.plusHours(23).plusMinutes(59).plusSeconds(59);

        NumberExpression<Double> yesCount =
                bonVoteQ.bonVoteType.eq(BonVoteType.YES).count().castToNum(Double.class);
        NumberExpression<Double> totalCount =
                bonVoteQ.id.count().castToNum(Double.class);

        query.where(bonVoteQ.createdAt.between(startOfYesterday, endOfYesterday)) // 어제의 투표에 대해 필터링
                .groupBy(bonQ.id)
                .having(yesCount.divide(totalCount).between(0.4, 0.6));

        return query.fetch();
    }

    @Override
    public Bon findNextCursor(Long memberId, Long lastCursor, SearchType searchType) {
        final var query = super.from(bonQ);

        switch (searchType) {
            case ALL:
                query.where(nextCursorLt(lastCursor));
                break;
            case MY_BON:
                query.where(nextCursorLt(lastCursor).and(bonQ.member.id.eq(memberId)));
                break;
            case VOTED:
                query.where(nextCursorLt(lastCursor).and(bonQ.bonVotes.any().member.id.eq(memberId)));
                break;
            case NOT_VOTED:
                query.where(nextCursorLt(lastCursor).and(bonQ.bonVotes.any().member.id.eq(memberId).not()));
                break;
        }

        return query.orderBy(bonQ.id.desc())
                .fetchFirst();
    }

    private BooleanExpression nextCursorLoe(Long nextCursor) {
        return nextCursor != null ? bonQ.id.loe(nextCursor) : null;
    }

    private BooleanExpression nextCursorLt(Long nextCursor) {
        return nextCursor != null ? bonQ.id.lt(nextCursor) : null;
    }

}
