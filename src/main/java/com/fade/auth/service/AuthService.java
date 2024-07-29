package com.fade.auth.service;

import com.fade.global.component.JwtTokenProvider;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import com.fade.member.entity.RefreshToken;
import com.fade.member.service.MemberCommonService;
import com.fade.member.vo.MemberJwtClaim;
import com.fade.member.repository.RefreshTokenRepository;
import com.fade.member.service.MemberService;
import com.fade.sociallogin.constant.SocialType;
import com.fade.sociallogin.dto.request.SignupByCodeRequest;
import com.fade.sociallogin.dto.response.SigninResponse;
import com.fade.sociallogin.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final MemberCommonService memberCommonService;
    private final SocialLoginService socialLoginService;

    @Transactional
    public SigninResponse signin(Long memberId) {
        return new SigninResponse(
                this.generateAccessToken(memberId),
                this.generateRefreshToken(memberId)
        );
    }

    @Transactional
    public SigninResponse signup(
            SocialType socialType,
            SignupByCodeRequest signupByCodeRequest
    ) {
        final Long memberId = this.memberService.createUser(
                signupByCodeRequest.username(),
                signupByCodeRequest.genderType()
        );
        this.socialLoginService.create(
                memberId,
                socialType,
                signupByCodeRequest.socialAccessToken()
        );

        return this.signin(memberId);
    }

    public String generateAccessToken(Long memberId) {
        final var memberVo = this.memberService.findUserVo(memberId);

        final var memberClaim = new MemberJwtClaim(
                memberVo.getId(),
                memberVo.getGenderType(),
                memberVo.getMemberUsername(),
                memberVo.getMemberRoles()
        );

        return this.jwtTokenProvider.createToken(memberClaim);
    }

    public String generateAccessToken(String refreshToken) {
        this.verifyRefreshToken(refreshToken);

        final var rt = this.refreshTokenRepository.findByToken(refreshToken).get();

        return this.generateAccessToken(rt.getMember().getId());
    }

    @Transactional
    public Optional<String> generateRefreshTokenOrEmpty(String refreshToken) {
        this.verifyRefreshToken(refreshToken);

        final var rt = this.refreshTokenRepository.findByToken(refreshToken).get();

        if (!LocalDateTime.now().minus(Duration.ofDays(3)).isAfter(rt.getExpiredAt())) {
            return Optional.empty();
        }

        this.removeRefreshToken(rt.getToken());

        return Optional.of(
                this.generateRefreshToken(rt.getMember().getId())
        );
    }

    @Transactional
    public String generateRefreshToken(Long memberId) {
        final var member = this.memberCommonService.findById(memberId);

        final var refreshToken = DigestUtils
                .md5DigestAsHex(UUID.randomUUID().toString().getBytes());

        final var refreshTokenEntity = new RefreshToken(
                refreshToken,
                member,
                Duration.ofDays(14)
        );

        this.refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }

    private void verifyRefreshToken(String refreshToken) {
        final var rt = this.refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_REFRESH_TOKEN));

        if (LocalDateTime.now().isAfter(rt.getExpiredAt())) {
            throw new ApplicationException(ErrorCode.TOKEN_EXPIRED_ERROR);
        }
    }

    @Transactional
    public void removeRefreshToken(String refreshToken) {
        this.refreshTokenRepository.deleteByToken(refreshToken);
    }
}
