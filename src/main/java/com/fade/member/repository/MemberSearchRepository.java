package com.fade.member.repository;

import com.fade.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberSearchRepository extends JpaRepository<Member, Long> {
    List<Member> findTop5ByUsernameStartingWithOrderByUsernameAsc(String username);
}