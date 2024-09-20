package com.fade.bon.repository;

import com.fade.bon.entity.BonVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonVoteRepository extends JpaRepository<BonVote, Long>, CustomBonVoteRepository {

    Boolean existsByBonIdAndMemberId(Long bonId, Long memberId);
    BonVote findByBonIdAndMemberId(Long bonId, Long memberId);
}
