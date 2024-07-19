package com.fade.global.vo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuthProperty {
    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl;
    private final String profileUrl;
}
