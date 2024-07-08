package com.fade.sociallogin.dto.response;

public record SigninResponse(
        String accessToken,
        String refreshToken
) {
}
