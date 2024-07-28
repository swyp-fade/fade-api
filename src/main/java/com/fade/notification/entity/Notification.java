package com.fade.notification.entity;

import com.fade.feed.entity.Feed;
import com.fade.member.entity.Member;
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

    @Column(name = "isRead", nullable = false)
    private Boolean isRead;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt = LocalDateTime.now().plusMonths(1);

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member receiver;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public Notification(NotificationType type, Boolean isRead, Member receiver) {
        this.type = type;
        this.isRead = isRead;
        this.receiver = receiver;
    }

    public void readNotification() {
        this.isRead = true;
    }
}
