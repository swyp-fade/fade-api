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

    // 현재는 CommentId만 존재
    private Long targetId;

    // 좋아요, 좋아요 취소
    private Boolean liked;

    @Builder
    public BonCommentLike(Member member, Long targetId, Boolean liked) {
        this.member = member;
        this.targetId = targetId;
        this.liked = liked;
    }
}
