package com.fade.sociallogin.controller;

import com.fade.sociallogin.constant.SocialType;
import com.fade.sociallogin.dto.request.ExistsSocialLoginRequest;
import com.fade.sociallogin.dto.request.SigninByCodeRequest;
import com.fade.sociallogin.dto.request.SignupByCodeRequest;
import com.fade.sociallogin.dto.response.ExistsSocialLoginResponse;
import com.fade.sociallogin.dto.response.SigninResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tags({
        @Tag(name = "Social Login")
})
@RequestMapping("social-login")
public class SocialLoginController {
    @PostMapping("/{socialType}/signin/by-code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                schema = @Schema(implementation = SigninResponse.class)
            ))
    })
    public SigninResponse signinByCode(
            @PathVariable("socialType") SocialType socialType,
            SigninByCodeRequest signinByCodeRequest
    ) {
        return null;
    }

    @PostMapping("/{socialType}/signup/by-code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = SigninResponse.class)
            ))
    })
    public SigninResponse signupByCode(
            @PathVariable("socialType") SocialType socialType,
            SignupByCodeRequest signupByCodeRequest
    ) {
        return null;
    }

    @GetMapping("/{socialType}/exists")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = ExistsSocialLoginResponse.class)
            ))
    })
    public ExistsSocialLoginResponse hasSocialLoginInfo(
            @PathVariable("socialType") SocialType socialType,
            ExistsSocialLoginRequest existsSocialLoginRequest
    ) {
        return null;
    }
}
