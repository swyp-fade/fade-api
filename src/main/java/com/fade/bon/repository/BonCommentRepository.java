package com.fade.bon.repository;

import com.fade.bon.entity.BonComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonCommentRepository extends JpaRepository<BonComment, Long>, CustomBonCommentRepository {
    boolean existsByIdAndMemberId(Long id, Long memberId);
}
