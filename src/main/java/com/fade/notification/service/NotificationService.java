package com.fade.notification.service;

import com.fade.member.service.MemberCommonService;
import com.fade.notification.constant.NotificationStatus;
import com.fade.notification.dto.CreateNotificationDto;
import com.fade.notification.dto.response.FindNotificationResponse;
import com.fade.notification.entity.Notification;
import com.fade.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final MemberCommonService memberCommonService;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public FindNotificationResponse findNotifications(Long memberId, Long cursor, int limit) {
        final var member = memberCommonService.findById(memberId);
        final var notifications = notificationRepository.findNotificationsUsingNoOffset(member.getId(), cursor, limit);

        return new FindNotificationResponse(
                notifications.stream().map(notification -> new FindNotificationResponse.FindNotificationItemsResponse(
                        notification.getId(),
                        null,
                        notification.getType(),
                        notification.getCreatedAt()
                )).toList(),
                !notifications.isEmpty() ? notifications.get(notifications.size() - 1).getId() : null
        );
    }

    @Transactional
    public void createNotification(CreateNotificationDto createNotificationDto) {
        final var receiver = memberCommonService.findById(createNotificationDto.receiverId());

        Notification notification = Notification.builder()
                .type(createNotificationDto.type())
                .status(NotificationStatus.UNREAD)
                .receiver(receiver)
                .build();

        notificationRepository.save(notification);
    }
}
