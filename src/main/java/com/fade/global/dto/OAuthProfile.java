package com.fade.global.dto;

import com.fade.sociallogin.constant.SocialType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class OAuthProfile {
    private final String code;
    private final Map<String, Object> rawData;
    private final SocialType socialType;
    private final ByteArrayResource profileImage;
}
