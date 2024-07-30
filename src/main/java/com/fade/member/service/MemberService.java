package com.fade.member.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.faparchiving.repository.FapArchivingRepository;
import com.fade.global.constant.ErrorCode;
import com.fade.global.constant.GenderType;
import com.fade.global.exception.ApplicationException;
import com.fade.member.constant.MemberRole;
import com.fade.member.dto.request.ModifyMemberRequest;
import com.fade.member.dto.response.FindMemberDetailResponse;
import com.fade.member.dto.response.MemberSearchItemResponse;
import com.fade.member.dto.response.MemberSearchResponse;
import com.fade.member.entity.Member;
import com.fade.member.repository.MemberRepository;
import com.fade.member.repository.MemberSearchRepository;
import com.fade.member.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberCommonService memberCommonService;
    private final AttachmentService attachmentService;

    private final MemberSearchRepository memberSearchRepository;
    private final FapArchivingRepository fapArchivingRepository;

    @Transactional
    public Long createUser(
            String username,
            GenderType gender
    ) {
        final var member = new Member(username, gender);

        this.memberRepository.save(member);

        return member.getId();
    }

    public FindMemberDetailResponse findMemberDetail(Long memberId) {
        final var member = this.memberCommonService.findById(memberId);

        String profileImageUrl = null;
        try {
            profileImageUrl = this.attachmentService.getUrl(
                    member.getId(),
                    AttachmentLinkableType.USER,
                    AttachmentLinkType.PROFILE
            );
        } catch (ApplicationException e) {
            if (!e.getErrorCode().equals(ErrorCode.NOT_FOUND_ATTACHMENT)) {
                throw e;
            }
        }

        return new FindMemberDetailResponse(
                member.getId(),
                member.getGenderType(),
                member.getUsername(),
                profileImageUrl,
                countFapArchivingByMemberId(member.getId())
        );
    }

    @Transactional
    public Long modifyMember(Long memberId, ModifyMemberRequest modifyMemberRequest) {
        final var member = this.memberCommonService.findById(memberId);

        if (modifyMemberRequest.username() != null) {
            member.modifyUsername(modifyMemberRequest.username());
        }

        if (modifyMemberRequest.profileImageAttachmentId() != null) {
            if (this.attachmentService.existsLinkable(
                    member.getId(),
                    AttachmentLinkableType.USER,
                    AttachmentLinkType.PROFILE
            )) {
                this.attachmentService.unLink(
                        member.getId(),
                        AttachmentLinkableType.USER,
                        AttachmentLinkType.PROFILE
                );
            }

            this.attachmentService.linkAttachment(
                    modifyMemberRequest.profileImageAttachmentId(),
                    AttachmentLinkableType.USER,
                    AttachmentLinkType.PROFILE,
                    member.getId()
            );
        }

        this.memberRepository.save(member);

        return member.getId();
    }

    public UserVo findUserVo(Long memberId) {
        final var member = this.memberCommonService.findById(memberId);

        return new UserVo(member.getId(), member.getUsername(), member.getGenderType(), List.of(MemberRole.USER));
    }

    public MemberSearchResponse searchMembers(String query) {
        List<MemberSearchItemResponse> matchedMembers = memberSearchRepository.findTop5ByUsernameStartingWithOrderByUsernameAsc(query)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new MemberSearchResponse(matchedMembers);
    }

    private MemberSearchItemResponse mapToResponse(Member member) {
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
        return new MemberSearchItemResponse(member.getUsername(), profileImageUrl);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        final var member = this.memberCommonService.findById(memberId);

        member.withdraw();

        this.memberRepository.save(member);
    }

    private Long countFapArchivingByMemberId(Long memberId) {
        return fapArchivingRepository.countByMemberId(memberId);
    }
}
