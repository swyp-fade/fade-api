package com.fade.bon.entity;

import com.fade.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "bon_comment")
@Entity
@Getter
@NoArgsConstructor
public class BonComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bon_id", nullable = false)
    private Bon bon;

    @Column(name = "is_mine", nullable = false)
    private Boolean isMine;

    @Builder
    public BonComment(Member member, String content, Bon bon, Boolean isMine) {
        this.member = member;
        this.content = content;
        this.bon = bon;
        this.isMine = isMine;
    }
}
