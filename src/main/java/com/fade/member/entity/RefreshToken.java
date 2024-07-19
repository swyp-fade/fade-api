package com.fade.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @TODO: 추후 redis로 변경
 */
@Table(name = "refresh_tokens")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private String token;

    @ManyToOne()
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public RefreshToken(
            String token,
            Member member,
            Duration expiredDuration
    ) {
        this.token = token;
        this.member = member;
        this.expiredAt = LocalDateTime.now().plus(expiredDuration);
    }
}
