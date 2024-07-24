package com.fade.subscribe.repository;

import com.fade.subscribe.entity.Subscribe;

import java.util.List;

public interface SubscribeRepositoryCustom {
    List<Subscribe> findSubscribersUsingNoOffset(Long memberId, Long nextCursor, int limit);
}
