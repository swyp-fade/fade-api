package com.fade.report.service;

import com.fade.feed.service.FeedCommonService;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import com.fade.member.service.MemberCommonService;
import com.fade.notification.constant.NotificationType;
import com.fade.notification.dto.CreateNotificationDto;
import com.fade.report.constant.ReportType;
import com.fade.report.dto.request.CountReportRequest;
import com.fade.report.entity.Report;
import com.fade.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberCommonService memberCommonService;
    private final FeedCommonService feedCommonService;
    private final ApplicationEventPublisher eventPublisher;

    private static final int MAX_FEED_DELETE_COUNT = 3;

    @Transactional
    public Long createReport(
            Long memberId,
            Long feedId,
            ReportType reportType,
            String cause
    ) {
        if (this.reportRepository.existsByMemberIdAndFeedId(memberId, feedId)) {
            throw new ApplicationException(ErrorCode.ALREADY_EXISTS_REPORT);
        }

        final var report = new Report(
                this.memberCommonService.findById(memberId),
                this.feedCommonService.findById(feedId),
                reportType,
                cause
        );

        this.reportRepository.save(report);
        notifyFeedReport(report, createFeedReportNotificationDto(report.getFeed().getMember().getId(), feedId));

        if (this.count(CountReportRequest.builder().feedId(feedId).build()) >= MAX_FEED_DELETE_COUNT) {
            report.getFeed().remove();
            notifyFeedDelete(report, createFeedDeleteNotificationDto(report.getFeed().getMember().getId(), feedId));
        }

        return report.getId();
    }

    public Long count(CountReportRequest countReportRequest) {
        return this.reportRepository.countByCondition(countReportRequest);
    }


    private void notifyFeedReport(Report report, CreateNotificationDto createNotificationDto) {
        report.publishEvent(eventPublisher, createNotificationDto);
    }

    private void notifyFeedDelete(Report report, CreateNotificationDto createNotificationDto) {
        report.publishEvent(eventPublisher, createNotificationDto);
    }

    private CreateNotificationDto createFeedReportNotificationDto(Long receiverId, Long feedId) {
        return CreateNotificationDto.builder()
                .receiverId(receiverId)
                .feedId(feedId)
                .type(NotificationType.FEED_REPORTED)
                .build();
    }

    private CreateNotificationDto createFeedDeleteNotificationDto(Long receiverId, Long feedId) {
        return CreateNotificationDto.builder()
                .receiverId(receiverId)
                .feedId(feedId)
                .type(NotificationType.FEED_DELETED)
                .build();
    }
}
