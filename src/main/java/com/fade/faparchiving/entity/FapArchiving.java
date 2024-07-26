package com.fade.faparchiving.entity;

import com.fade.feed.entity.Feed;
import com.fade.member.entity.Member;
import com.fade.notification.dto.CreateNotificationDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FapArchiving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "archived_at")
    private LocalDateTime archivedAt = LocalDateTime.now().minusDays(1);

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public FapArchiving(Member member, Feed feed) {
        this.member = member;
        this.feed = feed;
    }

    public void publishEvent(ApplicationEventPublisher eventPublisher, CreateNotificationDto createNotificationDto) {
        eventPublisher.publishEvent(createNotificationDto);
    }
}
