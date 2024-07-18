package com.fade.bookmark.entity;

import com.fade.feed.entity.Feed;
import com.fade.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "bookmark",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"member_id", "feed_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bookmarked_at")
    private LocalDateTime bookMarkedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public Bookmark(Member member, Feed feed) {
        this.member = member;
        this.feed = feed;
    }
}
