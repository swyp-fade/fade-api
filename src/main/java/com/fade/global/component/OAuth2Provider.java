package com.fade.global.component;

import com.fade.global.dto.OAuthProfile;

public interface OAuth2Provider {
    String getAccessToken(String code, String redirectUri);
    OAuthProfile getProfile(String accessToken);
}
