package com.fade.sociallogin.service;

import com.fade.global.component.JwtTokenProvider;
import com.fade.global.component.impl.KakaoOAuth2Provider;
import com.fade.global.constant.ErrorCode;
import com.fade.global.constant.GenderType;
import com.fade.global.dto.OAuthProfile;
import com.fade.global.exception.ApplicationException;
import com.fade.member.entity.Member;
import com.fade.member.service.MemberCommonService;
import com.fade.member.service.MemberService;
import com.fade.sociallogin.constant.SocialType;
import com.fade.sociallogin.dto.request.SignupByCodeRequest;
import com.fade.sociallogin.dto.response.SigninResponse;
import com.fade.sociallogin.entity.SocialLogin;
import com.fade.sociallogin.repository.SocialLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialLoginService {
    private final SocialLoginRepository socialLoginRepository;
    private final KakaoOAuth2Provider kakaoOAuth2Provider;
    private final MemberService memberService;
    private final MemberCommonService memberCommonService;

    @Transactional
    public SigninResponse signinByCode(
            SocialType socialType,
            String code,
            String redirectUri
    ) {
        final var profile = this.getProfile(
                code,
                redirectUri,
                socialType
        );

        final var socialLogin = this.socialLoginRepository.findByCodeAndSocialType(
                profile.getCode(),
                profile.getSocialType()
        ).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_MATCH_SOCIAL_MEMBER));

        return this.memberService.signin(socialLogin.getMember().getId());
    }

    @Transactional
    public SigninResponse signupByCode(
            SocialType socialType,
            String code,
            SignupByCodeRequest signupByCodeRequest
    ) {
        final var profile = this.getProfile(
                code,
                signupByCodeRequest.redirectUri(),
                socialType
        );

        if (this.socialLoginRepository.existsByCodeAndSocialType(
                profile.getCode(),
                profile.getSocialType()
        )) {
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_MEMBER);
        }

        final var memberId = this.memberService.signup(
                signupByCodeRequest.username(),
                signupByCodeRequest.genderType()
        );
        final var socialLogin = new SocialLogin(
                code,
                socialType,
                profile.getRawData(),
                this.memberCommonService.findById(
                        memberId
                )
        );

        this.socialLoginRepository.save(socialLogin);

        return this.memberService.signin(memberId);
    }

    public boolean hasSocialLoginInfo(SocialType socialType, String code) {
        return socialLoginRepository.existsByCodeAndSocialType(code, socialType);
    }

    /**
     * TODO: 추후 더 객체지향 적으로 수정
     */
    private OAuthProfile getProfile(String code, String redirectUri, SocialType socialType) {
        final OAuthProfile profile;

        switch (socialType) {
            case KAKAO:
                String socialAccessToken =
                        this.kakaoOAuth2Provider.getAccessToken(code, redirectUri);
                profile = this.kakaoOAuth2Provider.getProfile(socialAccessToken);
                break;
            default:
                throw new IllegalArgumentException("Unsupported social type: " + socialType);
        }

        return profile;
    }
}