package com.fade.notification.repository;

import com.fade.notification.entity.Notification;

import java.util.List;

public interface NotificationRepositoryCustom {
    List<Notification> findNotificationsUsingNoOffset(Long memberId, Long nextCursor, int limit);
}
