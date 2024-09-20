package com.fade.bon.repository;

import com.fade.bon.dto.request.CommentCountRequest;
import com.fade.bon.dto.request.FindBonCommentRequest;
import com.fade.bon.entity.BonComment;

import java.util.List;

public interface CustomBonCommentRepository {
    List<BonComment> findBonComments(Long bonId, FindBonCommentRequest findBonCommentRequest);
    List<BonComment> findBestBonComments(Long bonId);
    BonComment findNextCursor(Long bonCommentId);
    Long countByCondition(CommentCountRequest commentCountRequest);
}
