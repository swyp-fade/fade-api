package com.fade.member.entity;

import com.fade.global.constant.GenderType;
import com.fade.sociallogin.entity.SocialLogin;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "members")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE members SET deleted_at=NOW() WHERE id=?")
@SQLRestriction("deleted_at IS NULL")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 계정id
    @Column(name = "name", nullable = false)
    private String username;

    @Column(name = "gender_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @OneToMany(mappedBy = "member")
    private Set<SocialLogin> socialLogins = new HashSet<>();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "introduce_content", length = 500)
    private String introduceContent;

    public Member(String username, GenderType genderType) {
        this.username = username;
        this.genderType = genderType;
    }

    public void modifyUsername(String username) {
        this.username = username;
    }

    public void withdraw() {
        this.deletedAt = LocalDateTime.now();
    }

    public void modifyIntroduceContent(String introduceContent) {
        this.introduceContent = introduceContent;
    }
}
