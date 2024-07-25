package com.fade.membersearch.service;

import com.fade.membersearch.dto.response.MemberSearchResponse;
import com.fade.membersearch.repository.MemberSearchRepository;

import com.fade.membersearch.repository.MemberSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSearchService {
    private final MemberSearchRepository memberRepository;

    public List<MemberSearchResponse> searchMembers(String query) {
        return memberRepository.findTop5ByUsernameStartingWithOrderByUsernameAsc(query)
                .stream()
                .map(member -> new MemberSearchResponse(member.getUsername()))
                .collect(Collectors.toList());
    }
}