package com.fade.auth.controller;

import com.fade.auth.dto.request.CreateAccessTokenByRefreshTokenRequest;
import com.fade.auth.dto.response.HttpSigninInResponse;
import com.fade.auth.service.AuthService;
import com.fade.sociallogin.constant.SocialType;
import com.fade.sociallogin.dto.request.SigninByCodeRequest;
import com.fade.sociallogin.dto.request.SignupByCodeRequest;
import com.fade.sociallogin.service.SocialLoginService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final SocialLoginService socialLoginService;
    private final AuthService authService;

    @PostMapping("/social-login/{socialType}/signin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = HttpSigninInResponse.class)
            ))
    })
    public HttpSigninInResponse signin(
            @PathVariable("socialType") SocialType socialType,
            @RequestBody
            @Valid
            SigninByCodeRequest signinByCodeRequest,
            HttpServletResponse response
    ) {
        final var signinRes = this.socialLoginService.signinByCode(
                socialType,
                signinByCodeRequest.code(),
                signinByCodeRequest.redirectUri()
        );

        this.setRefreshTokenCookie(response, signinRes.refreshToken());

        return new HttpSigninInResponse(signinRes.accessToken());
    }

    @PostMapping("/social-login/{socialType}/signup")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = HttpSigninInResponse.class)
            ))
    })
    public HttpSigninInResponse signup(
            @PathVariable("socialType") SocialType socialType,
            @RequestBody
            @Valid
            SignupByCodeRequest signupByCodeRequest,
            HttpServletResponse response
    ) {
        final var signinRes = this.socialLoginService.signupByCode(
                socialType,
                signupByCodeRequest
        );

        this.setRefreshTokenCookie(response, signinRes.refreshToken());

        return new HttpSigninInResponse(signinRes.accessToken());
    }

    @PostMapping("/token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = HttpSigninInResponse.class)
            ))
    })
    public HttpSigninInResponse generateAccessToken(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        final var token = this.authService.generateAccessToken(
                refreshToken
        );

        this.setRefreshTokenCookie(
                response,
                token.refreshToken()
        );

        return new HttpSigninInResponse(token.accessToken());
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setMaxAge(7*24*60*60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }
}
