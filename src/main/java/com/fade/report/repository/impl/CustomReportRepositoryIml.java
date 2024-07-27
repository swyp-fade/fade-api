package com.fade.report.repository.impl;

import com.fade.report.dto.request.CountReportRequest;
import com.fade.report.entity.QReport;
import com.fade.report.entity.Report;
import com.fade.report.repository.CustomReportRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CustomReportRepositoryIml extends QuerydslRepositorySupport implements CustomReportRepository {
    private QReport reportQ = QReport.report;

    public CustomReportRepositoryIml() {
        super(Report.class);
    }

    @Override
    public Long countByCondition(CountReportRequest countReportRequest) {
        return super
                .from(reportQ)
                .where(feedIdEq(countReportRequest.getFeedId()))
                .fetchCount();
    }

    private BooleanExpression feedIdEq(Long feedId) {
        return feedId != null ? reportQ.feed.id.eq(feedId) : null;
    }
}
