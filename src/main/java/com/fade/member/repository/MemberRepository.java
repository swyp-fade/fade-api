package com.fade.member.repository;

import com.fade.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findTop5ByUsernameStartingWithOrderByUsernameAsc(String username);
    Boolean existsByUsername(String username);
    Boolean existsByUsernameAndIdNot(String username, Long id);

    @Query(value = "SELECT m.deleted_at IS NOT NULL FROM members m WHERE m.id = :memberId", nativeQuery = true)
    Integer isDeletedMember(Long memberId);

}