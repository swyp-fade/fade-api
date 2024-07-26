package com.fade.notification.repository;

import com.fade.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom{
    List<Notification> findByReceiverId(Long receiverId);
}
