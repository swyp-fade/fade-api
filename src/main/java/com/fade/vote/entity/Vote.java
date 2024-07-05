package com.fade.vote.entity;

import com.fade.vote.constant.VoteType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type")
    private VoteType voteType;

    @Column(name = "voted_at")
    private LocalDateTime votedAt;

//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member;

//    @ManyToOne
//    @JoinColumn(name = "post_id")
//    private Post post;

    @Builder
    public Vote(VoteType voteType, LocalDateTime votedAt) {
        this.voteType = voteType;
        this.votedAt = votedAt;
    }
}
