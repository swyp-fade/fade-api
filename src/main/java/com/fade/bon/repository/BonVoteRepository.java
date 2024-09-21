package com.fade.bon.repository;

import com.fade.bon.entity.BonVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BonVoteRepository extends JpaRepository<BonVote, Long>, CustomBonVoteRepository {

    Boolean existsByBonIdAndMemberId(Long bonId, Long memberId);
    Optional<BonVote> findByBonIdAndMemberId(Long bonId, Long memberId);
}
