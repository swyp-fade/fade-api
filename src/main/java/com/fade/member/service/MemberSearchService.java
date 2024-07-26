package com.fade.member.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import com.fade.member.dto.response.MemberSearchResponse;
import com.fade.member.entity.Member;
import com.fade.member.repository.MemberSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSearchService {
    private final MemberSearchRepository memberSearchRepository;
    private final AttachmentService attachmentService;

    public List<MemberSearchResponse> searchMembers(String query) {
        return memberSearchRepository.findTop5ByUsernameStartingWithOrderByUsernameAsc(query)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MemberSearchResponse mapToResponse(Member member) {
        String profileImageUrl = null;
        try {
            profileImageUrl = attachmentService.getUrl(
                    member.getId(),
                    AttachmentLinkableType.USER,
                    AttachmentLinkType.PROFILE
            );
        } catch (ApplicationException e) {
            if (!e.getErrorCode().equals(ErrorCode.NOT_FOUND_ATTACHMENT)) {
                throw e;
            }
        }
        return new MemberSearchResponse(member.getUsername(), profileImageUrl);
    }
}