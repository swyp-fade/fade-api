package com.fade.global.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberJwtClaim implements IJwtClaim {
    private Long id;

    public MemberJwtClaim(Long id) {
        this.id = id;
    }
}