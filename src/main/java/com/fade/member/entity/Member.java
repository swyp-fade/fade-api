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

import java.util.HashSet;
import java.util.Set;

@Table(name = "members")
@Entity
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
}
