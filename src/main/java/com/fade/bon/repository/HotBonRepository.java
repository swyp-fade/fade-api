package com.fade.bon.repository;

import com.fade.bon.entity.HotBon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HotBonRepository extends JpaRepository<HotBon, Long>, CustomHotBonRepository {
    Boolean existsByBonId(Long bonId);
    @Modifying
    @Query("DELETE FROM HotBon h")
    void deleteAllHotBon();
}
