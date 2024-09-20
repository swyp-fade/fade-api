package com.fade.bon.repository.impl;

import com.fade.bon.dto.request.FindBonRequest;
import com.fade.bon.entity.HotBon;
import com.fade.bon.entity.QHotBon;
import com.fade.bon.repository.CustomHotBonRepository;
import com.fade.global.constant.SearchType;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HotBonRepositoryImpl extends QuerydslRepositorySupport implements CustomHotBonRepository {
    HotBonRepositoryImpl() {
        super(HotBon.class);
    }

    private final QHotBon hotBonQ = QHotBon.hotBon;


    @Override
    public List<HotBon> findHotBons(Long memberId, FindBonRequest findBonRequest) {
        final var query = super.from(hotBonQ);

        query.where(
                this.nextCursorLoe(findBonRequest.nextCursor())
        );

        switch (findBonRequest.searchType()) {
            case MY_BON:
                query.where(hotBonQ.bon.member.id.eq(memberId));
                break;
            case VOTED:
                query.where(hotBonQ.bon.bonVotes.any().member.id.eq(memberId));
                break;
            case NOT_VOTED:
                query.where(hotBonQ.bon.bonVotes.any().member.id.eq(memberId).not());
                break;
        }

        query.orderBy(hotBonQ.rank.desc());
        query.limit(findBonRequest.limit());
        return query.fetch();
    }

    @Override
    public HotBon findNextCursor(Long memberId, Long lastCursor, SearchType searchType) {
        final var query = super.from(hotBonQ);

        switch (searchType) {
            case MY_BON:
                query.where(nextCursorLt(lastCursor).and(hotBonQ.bon.member.id.eq(memberId)));
                break;
            case VOTED:
                query.where(nextCursorLt(lastCursor).and(hotBonQ.bon.bonVotes.any().member.id.eq(memberId)));
                break;
            case NOT_VOTED:
                query.where(nextCursorLt(lastCursor).and(hotBonQ.bon.bonVotes.any().member.id.eq(memberId).not()));
                break;
        }

        return query.orderBy(hotBonQ.rank.desc()).fetchFirst();
    }

    private BooleanExpression nextCursorLoe(Long nextCursor) {
        return nextCursor != null ? hotBonQ.rank.loe(nextCursor) : null;
    }

    private BooleanExpression nextCursorLt(Long nextCursor) {
        return nextCursor != null ? hotBonQ.rank.lt(nextCursor) : null;
    }
}
