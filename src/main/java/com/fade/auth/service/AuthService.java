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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Transactional
    public SigninResponse generateAccessToken(String refreshToken) {
        final var rtOrEmpty = this.refreshTokenRepository.findByToken(refreshToken);

        if (rtOrEmpty.isEmpty()) {
            return null;
        }

        final var rt = rtOrEmpty.get();

        if (LocalDateTime.now().isAfter(rt.getExpiredAt())) {
            throw new ApplicationException(ErrorCode.TOKEN_NOT_EXIST);
        }

        if (LocalDateTime.now().minus(Duration.ofDays(3)).isAfter(rt.getExpiredAt())) {
            this.refreshTokenRepository.delete(rt);

            return this.memberService.signin(rt.getMember().getId());
        }

        final var memberClaim = new MemberJwtClaim(
                rt.getMember().getId(),
                List.of(MemberRole.USER)
        );

        return new SigninResponse(
                this.jwtTokenProvider.createToken(memberClaim),
                refreshToken
        );
    }
}
