package com.fade.faparchiving.repository;

import com.fade.faparchiving.entity.FapArchiving;

import java.time.LocalDateTime;
import java.util.List;

public interface FapArchivingRepositoryCustom {
    List<FapArchiving> findFapArchivingItems(LocalDateTime startOfDate, LocalDateTime endOfDate);
    Long countByCondition(Long feedId);
    Long countByMemberId(Long memberId);
}
