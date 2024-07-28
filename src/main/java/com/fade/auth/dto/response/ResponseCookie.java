package com.fade.auth.dto.response;

import lombok.Builder;

@Builder
public record ResponseCookie(
    String refreshToken,
    int maxAge,
    boolean httpOnly,
    String path,
    String sameSite
) {
}
