package com.fade.report.repository;

import com.fade.report.dto.request.CountReportRequest;

public interface CustomReportRepository {
    Long countByCondition(CountReportRequest countReportRequest);
}
