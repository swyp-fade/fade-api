package com.fade.notification.service;

import com.fade.member.entity.Member;
import com.fade.member.service.MemberCommonService;
import com.fade.notification.constant.NotificationStatus;
import com.fade.notification.dto.CreateNotificationDto;
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
