package com.fade.vote.entity;

import com.fade.feed.entity.Feed;
import com.fade.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyPopularFeed {
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
    public DailyPopularFeed(Member member, Feed feed) {
        this.member = member;
        this.feed = feed;
    }
}
