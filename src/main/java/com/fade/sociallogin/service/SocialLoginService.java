package com.fade.sociallogin.service;

import com.fade.sociallogin.constant.SocialType;
import com.fade.sociallogin.dto.request.ExistsSocialLoginRequest;
import com.fade.sociallogin.dto.request.SigninByCodeRequest;
import com.fade.sociallogin.dto.request.SignupByCodeRequest;
import com.fade.sociallogin.dto.response.ExistsSocialLoginResponse;
import com.fade.sociallogin.dto.response.SigninResponse;
import com.fade.sociallogin.repository.SocialLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLoginService {
    private final SocialLoginRepository socialLoginRepository;

    public SigninResponse signinByCode(SocialType socialType, SigninByCodeRequest request) {
        // 로그인 로직 구현
        // 예시: 소셜 로그인 코드로 사용자 인증 및 토큰 생성
        String accessToken = "generatedAccessToken";
        String refreshToken = "generatedRefreshToken";
        return new SigninResponse(accessToken, refreshToken);
    }

    public SigninResponse signupByCode(SocialType socialType, SignupByCodeRequest request) {
        // 회원가입 로직 구현
        // 예시: 새로운 사용자를 생성하고 소셜 로그인 정보 저장
        String accessToken = "generatedAccessToken";
        String refreshToken = "generatedRefreshToken";
        return new SigninResponse(accessToken, refreshToken);
    }

    public ExistsSocialLoginResponse hasSocialLoginInfo(SocialType socialType, ExistsSocialLoginRequest request) {
        // 소셜 로그인 정보 존재 여부 확인 로직 구현
        boolean exists = socialLoginRepository.existsBySocialTypeAndCode(socialType, request.code());
        return new ExistsSocialLoginResponse(exists);
    }
}