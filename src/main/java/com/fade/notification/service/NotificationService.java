package com.fade.notification.service;

import com.fade.faparchiving.repository.FapArchivingRepository;
import com.fade.member.service.MemberCommonService;
import com.fade.notification.constant.NotificationType;
import com.fade.notification.dto.CreateNotificationDto;
import com.fade.notification.dto.response.FindNotificationResponse;
import com.fade.notification.entity.Notification;
import com.fade.notification.repository.NotificationRepository;
import com.fade.report.repository.ReportRepository;
import com.fade.subscribe.entity.Subscribe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final MemberCommonService memberCommonService;
    private final NotificationRepository notificationRepository;
    private final ReportRepository repository;
    private final FapArchivingRepository fapArchivingRepository;

    @Transactional(readOnly = true)
    public FindNotificationResponse findNotifications(Long memberId, Long cursor, int limit) {
        final var member = memberCommonService.findById(memberId);
        final var notifications = notificationRepository.findNotificationsUsingNoOffset(member.getId(), cursor, limit);

        return new FindNotificationResponse(
                notifications.stream().map(notification -> new FindNotificationResponse.FindNotificationItemsResponse(
                        notification.getId(),
                        notification.getFeed().getId(),
                        notification.getType(),
                        notification.getCreatedAt(),
                        notification.getIsRead(),
                        getReportCount(notification.getReceiver().getId(), notification.getFeed().getId(), notification.getType()),
                        getFapSelectedAt(notification.getCreatedAt(), notification.getType()),
                        getDeleteFapCount(notification.getFeed().getId(), notification.getType())
                )).toList(),
                findNextCursor(member.getId(), !notifications.isEmpty() ? notifications.get(notifications.size() - 1).getId() : null)
        );
    }

    @Transactional
    public void createNotification(CreateNotificationDto createNotificationDto) {
        final var receiver = memberCommonService.findById(createNotificationDto.receiverId());

        Notification notification = Notification.builder()
                .type(createNotificationDto.type())
                .isRead(false)
                .receiver(receiver)
                .build();

        notificationRepository.save(notification);
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

    private Long getReportCount(Long memberId, Long feedId, NotificationType type) {
        if (type == NotificationType.FEED_REPORTED) {
            return repository.countByMemberIdAndFeedId(memberId, feedId);
        }
        return null;
    }

    private Long getDeleteFapCount(Long feedId, NotificationType type) {
        if (type == NotificationType.FAP_DELETED) {
            return hasFapArchiving(feedId) ? fapArchivingRepository.countDeletedFeedsInFapArchiving() : null;
        }
        return null;
    }

    private boolean hasFapArchiving(Long feedId) {
        return fapArchivingRepository.existsByFeedId(feedId);
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
