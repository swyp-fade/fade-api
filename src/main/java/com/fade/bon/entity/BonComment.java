package com.fade.bon.entity;

import com.fade.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Table(
        name = "bon_comments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id,member_id")
        }
)
@Entity
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE bon_comments SET deleted_at=NOW() WHERE id=?")
@SQLRestriction("deleted_at IS NULL")
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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public BonComment(Member member, String content, Bon bon) {
        this.member = member;
        this.content = content;
        this.bon = bon;
    }
}
