package com.fade.bon.repository;

import com.fade.bon.entity.BonCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonCommentLikeRepository extends JpaRepository<BonCommentLike, Long>, CustomBonCommentLikeRepository {
    Boolean existsByBonCommentIdAndMemberId(Long bonCommentId, Long memberId);
    Integer deleteByBonCommentIdAndMemberId(Long bonCommentId, Long memberId);
}
