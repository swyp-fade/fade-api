package com.fade.member.service;

import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import com.fade.member.entity.Member;
import com.fade.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberCommonService {
    private final MemberRepository memberRepository;

    public Member findById(Long memberId) {
        return this.memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
