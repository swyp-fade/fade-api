package com.fade.bon.entity;

import com.fade.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "bon_comment_likes")
@Entity
@Getter
@NoArgsConstructor
public class BonCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bon_comment_id")
    private BonComment bonComment;

    @Builder
    public BonCommentLike(Member member, BonComment bonComment) {
        this.member = member;
        this.bonComment = bonComment;
    }
}
