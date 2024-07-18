package com.fade.subscribe.service;

import com.fade.subscribe.entity.Subscribe;
import com.fade.subscribe.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeCommonService {
    private final SubscribeRepository subscribeRepository;

    public Subscribe findByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId) {
        //TODO: 에러코드 추가 후 RuntimeException 수정 예정
        return this.subscribeRepository.findByFromMemberIdAndToMemberId(fromMemberId, toMemberId)
                .orElseThrow(() -> new RuntimeException("Not found Subscribe Information"));
    }
}
