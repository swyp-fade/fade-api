package com.fade.notification.listener;

import com.fade.notification.dto.CreateNotificationDto;
import com.fade.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @TransactionalEventListener
    @Async("notificationAsyncExecutor")
    public void handleNotificationTrigger(CreateNotificationDto createNotificationDto) {
        switch (createNotificationDto.type()) {
            case FEED_REPORTED:
                notificationService.createFeedReportedNotification(createNotificationDto);
                break;
            case FEED_DELETED:
                notificationService.createdFeedDeletedNotification(createNotificationDto);
                break;
            case FAP_SELECTED:
                notificationService.createFapSelectedNotification(createNotificationDto);
                break;
            case FAP_DELETED:
                notificationService.createdFapDeletedNotification(createNotificationDto);
                break;
        }
    }
}
