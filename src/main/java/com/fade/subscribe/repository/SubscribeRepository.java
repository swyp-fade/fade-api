package com.fade.subscribe.repository;

import com.fade.subscribe.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long>, SubscribeRepositoryCustom{
    Optional<Subscribe> findByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
    List<Subscribe> findByFromMemberId(Long fromMemberId);
    boolean existsByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
}
