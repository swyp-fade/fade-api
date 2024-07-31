package com.fade.subscribe.entity;

import com.fade.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "subscribe",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"from_member_id", "to_member_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subscribed_at")
    private LocalDateTime subScribedAt;

    @ManyToOne
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @Builder
    public Subscribe(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.subScribedAt = LocalDateTime.now();
    }
}
