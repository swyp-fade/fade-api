package com.fade.global.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJwtClaim implements IJwtClaim {
    private Long id;

    public UserJwtClaim(Long id) {
        this.id = id;
    }
}