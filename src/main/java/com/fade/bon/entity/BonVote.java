package com.fade.bon.entity;

import com.fade.bon.constant.BonVoteType;
import com.fade.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "bon_vote")
@Entity
@Getter
@NoArgsConstructor
public class BonVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "bon_id")
    private Bon bon;

    @Enumerated(EnumType.STRING)
    @Column(name = "bon_vote_type", nullable = false)
    private BonVoteType bonVoteType;

    @Builder
    public BonVote(Member member, Bon bon, BonVoteType bonVoteType) {
        this.member = member;
        this.bon = bon;
        this.bonVoteType = bonVoteType;
    }
}
