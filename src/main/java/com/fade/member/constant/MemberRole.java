package com.fade.member.constant;

import org.springframework.security.core.GrantedAuthority;

public enum MemberRole implements GrantedAuthority {
    USER(MemberRole.USER_TYPE);

    public static final String USER_TYPE = "USER";

    private final String authority;

    MemberRole(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
