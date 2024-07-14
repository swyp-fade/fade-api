package com.fade.bookmark.repository;


import com.fade.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkMemberRepository extends JpaRepository<Member, Long> {
}
