package com.fade.bon.repository;

import com.fade.bon.entity.Bon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonRepository extends JpaRepository<Bon, Long> {
}
