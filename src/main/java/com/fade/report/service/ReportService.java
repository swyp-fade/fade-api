package com.fade.report.service;

import com.fade.feed.service.FeedCommonService;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import com.fade.member.service.MemberCommonService;
import com.fade.report.dto.request.CountReportRequest;
import com.fade.report.entity.Report;
import com.fade.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberCommonService memberCommonService;
    private final FeedCommonService feedCommonService;

    private static final int MAX_FEED_DELETE_COUNT = 3;

    @Transactional
    public Long createReport(
            Long memberId,
            Long feedId,
            String cause
    ) {
        if (this.reportRepository.existsByMemberIdAndFeedId(memberId, feedId)) {
            throw new ApplicationException(ErrorCode.ALREADY_EXISTS_REPORT);
        }

        final var report = new Report(
                this.memberCommonService.findById(memberId),
                this.feedCommonService.findById(feedId),
                cause
        );

        this.reportRepository.save(report);

        if (this.count(CountReportRequest.builder().feedId(feedId).build()) >= MAX_FEED_DELETE_COUNT) {
            report.getFeed().remove();
        }

        return report.getId();
    }

    public Long count(CountReportRequest countReportRequest) {
        return this.reportRepository.countByCondition(countReportRequest);
    }
}
