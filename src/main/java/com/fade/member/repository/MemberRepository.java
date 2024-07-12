package com.fade.member.repository;

import com.fade.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일로 회원을 찾기
    Optional<Member> findByEmail(String email);

    // 사용자명으로 회원을 찾기
    Optional<Member> findByUsername(String username);

    // 이메일이 존재하는지 확인하기
    boolean existsByEmail(String email);

    // 사용자명이 존재하는지 확인하기
    boolean existsByUsername(String username);

}