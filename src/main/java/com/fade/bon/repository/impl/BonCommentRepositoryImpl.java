package com.fade.bon.repository.impl;

import com.fade.bon.dto.request.CommentCountRequest;
import com.fade.bon.dto.request.FindBonCommentRequest;
import com.fade.bon.entity.BonComment;
import com.fade.bon.entity.QBonComment;
import com.fade.bon.entity.QBonCommentLike;
import com.fade.bon.repository.CustomBonCommentRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BonCommentRepositoryImpl extends QuerydslRepositorySupport implements CustomBonCommentRepository {
    public BonCommentRepositoryImpl() {
        super(BonComment.class);
    }

    private final QBonComment bonCommentQ = QBonComment.bonComment;
    private final QBonCommentLike bonCommentLikeQ = QBonCommentLike.bonCommentLike;

    @Override
    public List<BonComment> findBonComments(Long bonId, FindBonCommentRequest findBonCommentRequest) {
        final var query = super.from(bonCommentQ);

        query.where(
                this.nextCursorLoe(findBonCommentRequest.nextCursor())
        );

        query.orderBy(bonCommentQ.id.desc());
        query.limit(findBonCommentRequest.limit());

        return query.fetch();
    }

    @Override
    public List<BonComment> findBestBonComments(Long bonId) {
        final var query = super.from(bonCommentQ)
                .leftJoin(bonCommentLikeQ).on(bonCommentLikeQ.bonComment.id.eq(bonCommentQ.id))
                .where(this.bonIdEq(bonId))
                .groupBy(bonCommentQ.id)
                .orderBy(bonCommentLikeQ.count().desc())
                .limit(3);
        return query.fetch();
    }

    @Override
    public BonComment findNextCursor(Long lastCursor) {
        final var query = super.from(bonCommentQ);
        return query.orderBy(bonCommentQ.id.desc()).fetchFirst();
    }

    @Override
    public Long countByCondition(CommentCountRequest commentCountRequest) {
        final var query = super.from(bonCommentQ);

        query.where(this.bonIdEq(commentCountRequest.getBonId()));

        return query.fetchCount();
    }

    private BooleanExpression nextCursorLoe(Long nextCursor) {
        return nextCursor != null ? bonCommentQ.id.loe(nextCursor) : null;
    }

    private BooleanExpression bonIdEq(Long bonId) {
        return bonId != null ? bonCommentQ.bon.id.eq(bonId) : null;
    }
}
