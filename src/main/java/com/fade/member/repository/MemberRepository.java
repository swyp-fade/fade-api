package com.fade.member.repository;

import com.fade.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findTop5ByUsernameStartingWithOrderByUsernameAsc(String username);
    Boolean existsByUsername(String username);
    Boolean existsByUsernameAndIdNot(String username, Long id);
}