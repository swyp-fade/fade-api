package com.fade.auth.service;

import com.fade.global.component.JwtTokenProvider;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import com.fade.member.constant.MemberRole;
import com.fade.member.vo.MemberJwtClaim;
import com.fade.member.repository.RefreshTokenRepository;
import com.fade.member.service.MemberService;
import com.fade.sociallogin.dto.response.SigninResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public String generateAccessToken(String refreshToken) {
        final var rt = this.refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TOKEN_NOT_EXIST));

        if (LocalDateTime.now().isAfter(rt.getExpiredAt())) {
            throw new ApplicationException(ErrorCode.TOKEN_NOT_EXIST);
        }

        final var memberClaim = new MemberJwtClaim(
                rt.getMember().getId(),
                List.of(MemberRole.USER)
        );

        return this.jwtTokenProvider.createToken(memberClaim);
    }

    @Transactional
    public SigninResponse generateRefreshToken(String refreshToken) {
        final var rt = this.refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TOKEN_NOT_EXIST));

        if (LocalDateTime.now().isAfter(rt.getExpiredAt())) {
            throw new ApplicationException(ErrorCode.TOKEN_NOT_EXIST);
        }

        this.refreshTokenRepository.delete(rt);

        return this.memberService.signin(rt.getMember().getId());
    }
}
