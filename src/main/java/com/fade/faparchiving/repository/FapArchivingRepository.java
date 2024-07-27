package com.fade.faparchiving.repository;

import com.fade.faparchiving.entity.FapArchiving;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FapArchivingRepository extends JpaRepository<FapArchiving, Long>, FapArchivingRepositoryCustom {
    boolean existsByFeedId(Long feedId);

}
