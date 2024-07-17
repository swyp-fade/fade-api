package com.fade.auth.controller;

import com.fade.auth.dto.request.CreateAccessTokenByRefreshTokenRequest;
import com.fade.auth.dto.response.CreateAccessTokenByRefreshTokenResponse;
import com.fade.auth.service.AuthService;
import com.fade.sociallogin.constant.SocialType;
import com.fade.sociallogin.dto.request.ExistsSocialLoginRequest;
import com.fade.sociallogin.dto.request.SigninByCodeRequest;
import com.fade.sociallogin.dto.request.SignupByCodeRequest;
import com.fade.sociallogin.dto.response.ExistsSocialLoginResponse;
import com.fade.sociallogin.dto.response.SigninResponse;
import com.fade.sociallogin.service.SocialLoginService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
                    schema = @Schema(implementation = SigninResponse.class)
            ))
    })
    public SigninResponse signin(
            @PathVariable("socialType") SocialType socialType,
            SigninByCodeRequest signinByCodeRequest
    ) {
        return this.socialLoginService.signinByCode(
                socialType,
                signinByCodeRequest.code(),
                signinByCodeRequest.redirectUri()
        );
    }

    @PostMapping("/social-login/{socialType}/signup")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = SigninResponse.class)
            ))
    })
    public SigninResponse signup(
            @PathVariable("socialType") SocialType socialType,
            SignupByCodeRequest signupByCodeRequest
    ) {
        return this.socialLoginService.signupByCode(
                socialType,
                signupByCodeRequest.code(),
                signupByCodeRequest
        );
    }

    @GetMapping("/social-login/{socialType}/exists")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = ExistsSocialLoginResponse.class)
            ))
    })
    public ExistsSocialLoginResponse hasSocialLoginInfo(
            @PathVariable("socialType") SocialType socialType,
            ExistsSocialLoginRequest existsSocialLoginRequest
    ) {
        return new ExistsSocialLoginResponse(
                this.socialLoginService.hasSocialLoginInfo(socialType, existsSocialLoginRequest.code())
        );
    }

    @PostMapping("/token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = CreateAccessTokenByRefreshTokenResponse.class)
            ))
    })
    public CreateAccessTokenByRefreshTokenResponse generateAccessToken(
            CreateAccessTokenByRefreshTokenRequest createAccessTokenByRefreshTokenRequest
    ) {
        return new CreateAccessTokenByRefreshTokenResponse(
                this.authService.generateAccessToken(createAccessTokenByRefreshTokenRequest.refreshToken())
        );
    }

    @PostMapping("/token/refresh-token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = SigninResponse.class)
            ))
    })
    public SigninResponse generateRefreshToken(
            CreateAccessTokenByRefreshTokenRequest createAccessTokenByRefreshTokenRequest
    ) {
        return this.authService.generateRefreshToken(createAccessTokenByRefreshTokenRequest.refreshToken());
    }
}
