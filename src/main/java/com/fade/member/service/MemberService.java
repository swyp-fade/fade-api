package com.fade.member.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.global.component.JwtTokenProvider;
import com.fade.global.constant.ErrorCode;
import com.fade.global.constant.GenderType;
import com.fade.global.exception.ApplicationException;
import com.fade.member.constant.MemberRole;
import com.fade.member.dto.request.ModifyMemberRequest;
import com.fade.member.dto.response.FindMemberDetailResponse;
import com.fade.member.vo.MemberJwtClaim;
import com.fade.member.entity.Member;
import com.fade.member.entity.RefreshToken;
import com.fade.member.repository.MemberRepository;
import com.fade.member.repository.RefreshTokenRepository;
import com.fade.sociallogin.dto.response.SigninResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberCommonService memberCommonService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AttachmentService attachmentService;

    @Transactional
    public Long signup(
            String username,
            GenderType gender
    ) {
        final var member = new Member(username, gender);

        this.memberRepository.save(member);

        return member.getId();
    }

    @Transactional
    public SigninResponse signin(Long memberId) {
        final var member = memberCommonService.findById(memberId);

        final var memberClaim = new MemberJwtClaim(
                member.getId(),
                List.of(MemberRole.USER)
        );

        final var accessToken = this.jwtTokenProvider.createToken(memberClaim);
        final var refreshToken = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());

        final var refreshTokenEntity = new RefreshToken(
                refreshToken,
                member,
                Duration.ofDays(12)
        );

        this.refreshTokenRepository.save(refreshTokenEntity);

        return new SigninResponse(
                accessToken,
                refreshToken
        );
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
                profileImageUrl
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
}
