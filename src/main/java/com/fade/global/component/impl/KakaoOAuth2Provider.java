package com.fade.global.component.impl;

import com.fade.global.component.OAuth2Provider;
import com.fade.global.constant.ErrorCode;
import com.fade.global.dto.OAuthProfile;
import com.fade.global.exception.ApplicationException;
import com.fade.sociallogin.constant.SocialType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoOAuth2Provider implements OAuth2Provider {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kakao.oauth.client-id}")
    private String clientId;
    @Value("${kakao.oauth.client-secret}")
    private String clientSecret;
    @Value("${kakao.oauth.token-url}")
    private String tokenUrl;
    @Value("${kakao.oauth.profile-url}")
    private String profileUrl;

    @Override
    public String getAccessToken(String code, String redirectUri) {
        final var requestBody = new LinkedMultiValueMap<String, Object>();

        requestBody.set("grant_type", "authorization_code");
        requestBody.set("client_id", this.clientId);
        requestBody.set("redirect_uri", redirectUri);
        requestBody.set("code", code);
        requestBody.set("client_secret", this.clientSecret);

        final var headers = new HttpHeaders();
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        final var httpEntity = new HttpEntity<>(requestBody, headers);

        try {
            final var response = this.restTemplate.exchange(
                    this.tokenUrl,
                    HttpMethod.POST,
                    httpEntity,
                    Map.class
            );

            return Objects.requireNonNull((String) Objects.requireNonNull(response.getBody()).get("access_token"));
        } catch (HttpClientErrorException exception) {
            throw this.kakaoExceptionConverter(exception);
        }
    }

    @Override
    public OAuthProfile getProfile(String accessToken) {
        final var headers = new HttpHeaders();
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization", "Bearer " + accessToken);

        final var httpEntity = new HttpEntity<>(headers);

        try {
            final var response = this.restTemplate.exchange(
                    this.profileUrl,
                    HttpMethod.POST,
                    httpEntity,
                    Map.class
            );

            final var body = response.getBody();

            final var profileImage =
                    Optional.ofNullable((Map) body.get("kakao_account"))
                            .map((a) -> (Map) a.get("profile"))
                            .map((a) -> (String) a.get("profile_image_url"))
                            .map((imageUrl) ->  this.restTemplate.exchange(imageUrl, HttpMethod.GET, null, byte[].class).getBody())
                            .map(ByteArrayResource::new)
                    ;

            return new OAuthProfile(
                    Objects.requireNonNull(body).get("id") + "",
                    body,
                    SocialType.KAKAO,
                    profileImage.orElse(null)
            );
        } catch (HttpClientErrorException exception) {
            throw this.kakaoExceptionConverter(exception);
        }
    }

    /**
     * TODO: AOP로 전환 가능
     */
    private RuntimeException kakaoExceptionConverter(HttpClientErrorException exception) {
        try {
            final var error = this.objectMapper.readValue(exception.getResponseBodyAsString(), Map.class);
            final var errorCode = error.get("error_code");

            if (!Objects.isNull(errorCode)) {
                if (errorCode.equals("KOE320")) {
                    return new ApplicationException(ErrorCode.NOT_MATCH_OAUTH_CODE);
                }
                if (errorCode.equals("KOE006")) {
                    return new ApplicationException(ErrorCode.NOT_ALLOW_OAUTH_REDIRECT_URI);
                }
            }

            log.error(exception.getMessage(), exception);

            return new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            log.error(exception.getMessage(), exception);

            return new RuntimeException(e);
        }
    }
}
