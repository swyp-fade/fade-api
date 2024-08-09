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
import com.fade.member.dto.response.MemberSearchResponse;
import com.fade.member.dto.response.MemberSearchResponse.MemberSearchItemResponse;
import com.fade.member.entity.Member;
import com.fade.member.repository.MemberRepository;
import com.fade.member.repository.MemberSearchRepository;
import com.fade.member.vo.UserVo;
import com.fade.subscribe.dto.request.CountSubscriberRequest;
import com.fade.subscribe.service.SubscribeService;
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

    private final SubscribeService subscribeService;

    @Transactional
    public Long createUser(
            String username,
            GenderType gender
    ) {
        if (this.existsByUsername(username)) {
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_MEMBER_USERNAME);
        }

        final var member = new Member(username, gender);

        this.memberRepository.save(member);

        return member.getId();
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return this.memberRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username, Long ignoreMemberId) {
        return this.memberRepository.existsByUsernameAndIdNot(username, ignoreMemberId);
    }

    public FindMemberDetailResponse findMemberDetail(Long memberId, Long loginMemberId) {
        final var member = this.memberCommonService.findById(memberId);

        String profileImageUrl = null;
        if (this.attachmentService.existsLinkable(member.getId(),
                AttachmentLinkableType.USER,
                AttachmentLinkType.PROFILE)
        ) {
            profileImageUrl = this.attachmentService.getUrl(
                    member.getId(),
                    AttachmentLinkableType.USER,
                    AttachmentLinkType.PROFILE
            );
        }

        return FindMemberDetailResponse
                .builder()
                .id(member.getId())
                .genderType(member.getGenderType())
                .username(member.getUsername())
                .profileImageURL(profileImageUrl)
                .selectedFAPCount(countFapArchivingByMemberId(member.getId()))
                .deletedFAPCount(countDeletedFapArchivingByMemberId(member.getId()))
                .introduceContent(member.getIntroduceContent())
                .isSubscribed(subscribeService.hasSubscribe(loginMemberId, member.getId()))
                .subscribedCount(subscribeService.countSubscriber(
                        CountSubscriberRequest
                                .builder()
                                .toMemberId(member.getId())
                                .build()
                ))
                .build();
    }

    @Transactional
    public Long modifyMember(Long memberId, ModifyMemberRequest modifyMemberRequest) {
        final var member = this.memberCommonService.findById(memberId);

        if (modifyMemberRequest.username() != null) {
            if (this.existsByUsername(modifyMemberRequest.username(), memberId)) {
                throw new ApplicationException(ErrorCode.ALREADY_EXIST_MEMBER_USERNAME);
            }

            member.modifyUsername(modifyMemberRequest.username());
        }

        if (modifyMemberRequest.profileImageId() != null) {
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
                    modifyMemberRequest.profileImageId(),
                    AttachmentLinkableType.USER,
                    AttachmentLinkType.PROFILE,
                    member.getId()
            );
        }

        if (modifyMemberRequest.introduceContent() != null) {
            member.modifyIntroduceContent(modifyMemberRequest.introduceContent());
        }

        if (modifyMemberRequest.genderType() != null) {
            member.modifyGender(modifyMemberRequest.genderType());
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
        if (this.attachmentService.existsLinkable(member.getId(),
                AttachmentLinkableType.USER,
                AttachmentLinkType.PROFILE)
        ) {
            profileImageUrl = attachmentService.getUrl(
                    member.getId(),
                    AttachmentLinkableType.USER,
                    AttachmentLinkType.PROFILE
            );
        }
        return new MemberSearchItemResponse(member.getId(), member.getUsername(), profileImageUrl);
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

    private Long countDeletedFapArchivingByMemberId(Long memberId) {
        return fapArchivingRepository.countDeletedFapArchivingByMemberId(memberId);
    }
}
