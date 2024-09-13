package com.fade.like.entity;

import com.fade.like.constant.LikeType;
import com.fade.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "like")
@Entity
@Getter
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private LikeType likeType;

    // 현재는 CommentId만 존재
    private Long targetId;

    // 좋아요, 좋아요 취소
    private Boolean liked;

    @Builder
    public Like(Member member, LikeType likeType, Long targetId, Boolean liked) {
        this.member = member;
        this.likeType = likeType;
        this.targetId = targetId;
        this.liked = liked;
    }
}
