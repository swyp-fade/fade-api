package com.fade.bon.repository;

import com.fade.bon.entity.Bon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonRepository extends JpaRepository<Bon, Long>, CustomBonRepository {
    boolean existsByIdAndMemberId(Long id, Long memberId);
}
