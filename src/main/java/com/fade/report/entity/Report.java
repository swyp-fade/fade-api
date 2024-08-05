package com.fade.report.entity;

import com.fade.feed.entity.Feed;
import com.fade.member.entity.Member;
import com.fade.notification.dto.CreateNotificationDto;
import com.fade.report.constant.ReportType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationEventPublisher;

@Table(name = "reports")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    @Nullable
    private String cause;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @ManyToOne(optional = false)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    public Report(Member member, Feed feed, ReportType reportType, @Nullable String cause) {
        this.member = member;
        this.feed = feed;
        this.cause = cause;
        this.reportType = reportType;
    }

    public void publishEvent(ApplicationEventPublisher eventPublisher, CreateNotificationDto createNotificationDto) {
        eventPublisher.publishEvent(createNotificationDto);
    }
}
