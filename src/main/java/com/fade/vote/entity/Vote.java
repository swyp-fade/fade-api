package com.fade.vote.entity;

import com.fade.feed.entity.Feed;
import com.fade.member.entity.Member;
import com.fade.vote.constant.VoteType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type", nullable = false)
    private VoteType voteType;

    @Column(name = "voted_at")
    private LocalDateTime votedAt;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public Vote(Member member, Feed feed, VoteType voteType, LocalDateTime votedAt) {
        this.member = member;
        this.feed = feed;
        this.voteType = voteType;
        this.votedAt = votedAt;
    }
}
