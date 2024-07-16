package com.fade.sociallogin.entity;

import com.fade.member.entity.Member;
import com.fade.sociallogin.constant.SocialType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

@Table(
        name = "social_logins",
        uniqueConstraints = {
            @UniqueConstraint(
                columnNames = {"code", "social_type"}
            ),
            @UniqueConstraint(
                columnNames = {"member_id", "social_type"}
            ),
        }
)
@Entity
@Getter
public class SocialLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 128)
    private String code;

    @Column(name = "social_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "raw_data", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> rawData = new HashMap<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public SocialLogin(
            String code,
            SocialType socialType,
            Map<String, Object> rawData,
            Member member
    ) {
        this.code = code;
        this.socialType = socialType;
        this.rawData = rawData;
        this.member = member;
    }
}
