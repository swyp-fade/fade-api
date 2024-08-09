package com.fade.notification.service;

import com.fade.faparchiving.repository.FapArchivingRepository;
import com.fade.feed.service.FeedCommonService;
import com.fade.member.service.MemberCommonService;
import com.fade.notification.constant.NotificationType;
import com.fade.notification.dto.CreateNotificationDto;
import com.fade.notification.dto.response.FindNotificationResponse;
import com.fade.notification.entity.Notification;
import com.fade.notification.repository.NotificationRepository;
import com.fade.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final MemberCommonService memberCommonService;
    private final FeedCommonService feedCommonService;
    private final NotificationRepository notificationRepository;
    private final ReportRepository reportRepository;
    private final FapArchivingRepository fapArchivingRepository;

    @Transactional(readOnly = true)
    public FindNotificationResponse findNotifications(Long memberId, Long cursor, int limit) {
        final var member = memberCommonService.findById(memberId);
        final var notifications = notificationRepository.findNotificationsUsingNoOffset(member.getId(), cursor, limit);

        return new FindNotificationResponse(
                notifications.stream().map(notification -> new FindNotificationResponse.FindNotificationItemsResponse(
                        notification.getId(),
                        notification.getFeed() != null ? notification.getFeed().getId() : null,
                        notification.getType(),
                        notification.getCreatedAt(),
                        notification.getIsRead(),
                        getReportCount(
                                notification.getFeed() != null ? notification.getFeed().getId() : null,
                                notification.getType()),
                        getFapSelectedAt(notification.getCreatedAt(), notification.getType()),
                        getDeleteFapCount(member.getId(), notification.getType())
                )).toList(),
                findNextCursor(member.getId(), !notifications.isEmpty() ? notifications.get(notifications.size() - 1).getId() : null)
        );
    }

    @Transactional
    public void createFapSelectedNotification(CreateNotificationDto createNotificationDto) {
        final var receiver = memberCommonService.findById(createNotificationDto.receiverId());
        final var feed = feedCommonService.findById(createNotificationDto.feedId());

        notificationRepository.save(Notification.builder()
                .type(createNotificationDto.type())
                .isRead(false)
                .receiver(receiver)
                .feed(feed)
                .build());
    }

    @Transactional
    public void createFeedReportedNotification(CreateNotificationDto createNotificationDto) {
        final var receiver = memberCommonService.findById(createNotificationDto.receiverId());
        final var feed = feedCommonService.findById(createNotificationDto.feedId());

        notificationRepository.save(Notification.builder()
                .type(createNotificationDto.type())
                .isRead(false)
                .receiver(receiver)
                .feed(feed)
                .build());
    }

    @Transactional
    public void createdFeedDeletedNotification(CreateNotificationDto createNotificationDto) {
        final var receiver = memberCommonService.findById(createNotificationDto.receiverId());

        notificationRepository.save(Notification.builder()
                .type(createNotificationDto.type())
                .isRead(false)
                .receiver(receiver)
                .build());
    }

    @Transactional
    public void createdFapDeletedNotification(CreateNotificationDto createNotificationDto) {
        final var receiver = memberCommonService.findById(createNotificationDto.receiverId());

        notificationRepository.save(Notification.builder()
                .type(createNotificationDto.type())
                .isRead(false)
                .receiver(receiver)
                .build());
    }

    @Transactional
    public void readNotifications(Long memberId) {
        final var member = memberCommonService.findById(memberId);
        final var notifications = notificationRepository.findByReceiverId(member.getId());

        notifications.forEach(Notification::readNotification);
    }

    private LocalDateTime getFapSelectedAt(LocalDateTime notifiedAt, NotificationType type) {
        if (type == NotificationType.FAP_SELECTED) {
            return notifiedAt.minusDays(1);
        }
        return null;
    }

    private Long getReportCount(Long feedId, NotificationType type) {
        if (type == NotificationType.FEED_REPORTED) {
            return reportRepository.countByFeedId(feedId);
        }
        return null;
    }

    private Long getDeleteFapCount(Long memberId, NotificationType type) {
        if (type == NotificationType.FAP_DELETED) {
            return fapArchivingRepository.countDeletedFapArchivingByMemberId(memberId);
        }
        return null;
    }

    private Long findNextCursor(Long memberId, Long lastCursor) {
        if (lastCursor == null) {
            return null;
        }
        Notification nextCursorNotification = notificationRepository.findNextCursor(memberId, lastCursor);
        if (nextCursorNotification == null) {
            return null;
        }
        return nextCursorNotification.getId();
    }
}
