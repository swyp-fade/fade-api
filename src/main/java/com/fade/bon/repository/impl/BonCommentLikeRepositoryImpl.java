package com.fade.bon.repository.impl;

import com.fade.bon.dto.request.CommentLikeCountRequest;
import com.fade.bon.entity.BonCommentLike;
import com.fade.bon.entity.QBonCommentLike;
import com.fade.bon.repository.CustomBonCommentLikeRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class BonCommentLikeRepositoryImpl extends QuerydslRepositorySupport implements CustomBonCommentLikeRepository {

    BonCommentLikeRepositoryImpl() {super(BonCommentLike.class);}

    private final QBonCommentLike bonCommentLikeQ = QBonCommentLike.bonCommentLike;

    @Override
    public Long countByCondition(CommentLikeCountRequest commentLikeCountRequest) {
        final var query = super.from(bonCommentLikeQ);

        query.where(this.commentIdEq(commentLikeCountRequest.getCommentId()));

        return query.fetchCount();
    }

    private BooleanExpression commentIdEq(Long commentId) {return commentId != null ? bonCommentLikeQ.bonComment.id.eq(commentId) : null;}
}
