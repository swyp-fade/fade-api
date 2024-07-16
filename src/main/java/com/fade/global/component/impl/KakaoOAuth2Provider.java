package com.fade.global.component.impl;

import com.fade.global.component.OAuth2Provider;
import com.fade.global.dto.OAuthProfile;
import com.fade.sociallogin.constant.SocialType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2Provider implements OAuth2Provider {
    private final RestTemplate restTemplate;

    @Value("kakao.oauth.client-id")
    private String clientId;
    @Value("kakao.oauth.client-secret")
    private String clientSecret;
    @Value("kakao.oauth.token-url")
    private String tokenUrl;
    @Value("kakao.oauth.profile-url")
    private String profileUrl;

    @Override
    public String getAccessToken(String code, String redirectUri) {
        final var requestBody = Map.of(
                "grant_type", "authorization_code",
                "client_id", this.clientId,
                "redirect_uri", redirectUri,
                "code", code,
                "client_secret", this.clientSecret
        );
        final var headers = new HttpHeaders();
        headers.set("Content-type", "Content-type: application/x-www-form-urlencoded;charset=utf-8");

        final var httpEntity = new HttpEntity<>(requestBody, headers);

        final var response = this.restTemplate.exchange(
                this.tokenUrl,
                HttpMethod.POST,
                httpEntity,
                GetAccessTokenResponse.class
        );

        return Objects.requireNonNull(response.getBody()).getAccess_token();
    }

    @Override
    public OAuthProfile getProfile(String accessToken) {
        final var headers = new HttpHeaders();
        headers.set("Content-type", "Content-type: application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization", "Bearer " + accessToken);

        final var httpEntity = new HttpEntity<>(headers);

        final var response = this.restTemplate.exchange(
                this.profileUrl,
                HttpMethod.POST,
                httpEntity,
                Map.class
        );

        return new OAuthProfile(
            Objects.requireNonNull(response.getBody()).get("id") + "",
            response.getBody(),
            SocialType.KAKAO
        );
    }

    @RequiredArgsConstructor
    @Getter
    private static class GetAccessTokenResponse {
        private final String access_token;
    }
}
