package com.fade.notification.entity;

import com.fade.member.entity.Member;
import com.fade.notification.constant.NotificationStatus;
import com.fade.notification.constant.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt = LocalDateTime.now().plusMonths(1);

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member receiver;

    @Builder
    public Notification(NotificationType type, NotificationStatus status, Member receiver) {
        this.type = type;
        this.status = status;
        this.receiver = receiver;
    }
}
