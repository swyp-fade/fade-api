package com.fade.faparchiving.repository;

import com.fade.faparchiving.entity.FapArchiving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FapArchivingRepository extends JpaRepository<FapArchiving, Long>, FapArchivingRepositoryCustom {
    boolean existsByFeedId(Long feedId);
    @Query(value =
            "SELECT COUNT(*) FROM fap_archiving " +
                    "JOIN feeds ON feeds.id = fap_archiving.feed_id " +
                        "AND feeds.deleted_at IS NOT NULL " +
                    "WHERE fap_archiving.member_id=:memberId",
            nativeQuery = true
    )
    Long countDeletedFapArchivingByMemberId(@Param("memberId") Long memberId);
}
