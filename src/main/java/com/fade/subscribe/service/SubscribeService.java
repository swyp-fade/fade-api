package com.fade.subscribe.service;

import com.fade.member.entity.Member;
import com.fade.member.service.MemberCommonService;
import com.fade.subscribe.entity.Subscribe;
import com.fade.subscribe.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final MemberCommonService memberCommonService;
    private final SubscribeRepository subscribeRepository;
    private final SubscribeCommonService subscribeCommonService;

    @Transactional
    public Long subscribe(Long fromMemberId, Long toMemberId) {
        Member fromMember = memberCommonService.findById(fromMemberId);
        Member toMember = memberCommonService.findById(toMemberId);

        Subscribe subscribeFromMember = Subscribe.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();

        subscribeRepository.save(subscribeFromMember);

        return subscribeFromMember.getId();
    }

    @Transactional
    public void unSubscribe(Long fromMemberId, Long toMemberId) {
        Subscribe subscribe = subscribeCommonService.findByFromMemberIdAndToMemberId(fromMemberId, toMemberId);

        subscribeRepository.delete(subscribe);
    }
}