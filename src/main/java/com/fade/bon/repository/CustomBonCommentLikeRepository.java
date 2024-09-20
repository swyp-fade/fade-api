package com.fade.bon.repository;

import com.fade.bon.dto.request.CommentLikeCountRequest;

public interface CustomBonCommentLikeRepository {
    Long countByCondition(CommentLikeCountRequest commentLikeCountRequest);
}
