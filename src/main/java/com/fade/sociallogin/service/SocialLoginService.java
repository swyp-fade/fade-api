package com.fade.sociallogin.service;

import com.fade.attachment.service.AttachmentService;
import com.fade.global.component.impl.KakaoOAuth2Provider;
import com.fade.global.constant.ErrorCode;
import com.fade.global.dto.OAuthProfile;
import com.fade.global.exception.ApplicationException;
import com.fade.member.dto.request.ModifyMemberRequest;
import com.fade.member.service.MemberCommonService;
import com.fade.member.service.MemberService;
import com.fade.member.vo.UserVo;
import com.fade.sociallogin.constant.SocialType;
import com.fade.sociallogin.entity.SocialLogin;
import com.fade.sociallogin.repository.SocialLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialLoginService {
    private final SocialLoginRepository socialLoginRepository;
    private final KakaoOAuth2Provider kakaoOAuth2Provider;
    private final MemberService memberService;
    private final MemberCommonService memberCommonService;
    private final AttachmentService attachmentService;

    @Transactional
    public UserVo findUserVoByCode(
            SocialType socialType,
            String code,
            String redirectUri
    ) {
        final String socialAccessToken;

        switch (socialType) {
            case KAKAO:
                socialAccessToken = this.kakaoOAuth2Provider.getAccessToken(code, redirectUri);
                break;
            default:
                throw new IllegalArgumentException("Unsupported social type: " + socialType);
        }

        final var profile = this.getProfile(
                socialAccessToken,
                socialType
        );

        final var socialLogin = this.socialLoginRepository.findByCodeAndSocialType(
                profile.getCode(),
                profile.getSocialType()
        ).orElseThrow(() ->
                new ApplicationException(
                        ErrorCode.NOT_MATCH_SOCIAL_MEMBER,
                        Map.of(
                                "socialType", socialType.name(),
                                "socialAccessToken", socialAccessToken
                        )
                )
        );

        return this.memberService.findUserVo(socialLogin.getMember().getId());
    }

    @Transactional
    public Long create(
            Long memberId,
            SocialType socialType,
            String socialAccessToken
    ) {
        final var profile = this.getProfile(
                socialAccessToken,
                socialType
        );

        if (this.socialLoginRepository.existsByCodeAndSocialType(
                profile.getCode(),
                profile.getSocialType()
        )) {
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_MEMBER);
        }

        final var socialLogin = new SocialLogin(
                profile.getCode(),
                socialType,
                profile.getRawData(),
                this.memberCommonService.findById(
                        memberId
                )
        );

        if (profile.getProfileImage() != null) {
            final var attachmentId =
                    this.attachmentService.uploadFile(memberId, profile.getProfileImage());

            this.memberService.modifyMember(
                    memberId,
                    ModifyMemberRequest.builder().profileImageId(attachmentId).build()
            );
        }

        this.socialLoginRepository.save(socialLogin);

        return socialLogin.getId();
    }

    /**
     * TODO: 추후 더 객체지향 적으로 수정
     */
    private OAuthProfile getProfile(String socialAccessToken, SocialType socialType) {
        final OAuthProfile profile;

        switch (socialType) {
            case KAKAO:
                profile = this.kakaoOAuth2Provider.getProfile(socialAccessToken);
                break;
            default:
                throw new IllegalArgumentException("Unsupported social type: " + socialType);
        }

        return profile;
    }
}