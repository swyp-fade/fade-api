package com.fade.report.repository;

import com.fade.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, CustomReportRepository {
    boolean existsByMemberIdAndFeedId(Long memberId, Long feedId);
}
