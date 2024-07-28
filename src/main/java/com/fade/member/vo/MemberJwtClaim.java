package com.fade.member.vo;

import com.fade.global.constant.GenderType;
import com.fade.global.vo.IJwtClaim;
import com.fade.member.constant.MemberRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberJwtClaim implements IJwtClaim {
    private Long id;
    private GenderType genderType;
    private String username;
    private Collection<MemberRole> memberRoles;

    public MemberJwtClaim(
            Long id,
            GenderType genderType,
            String username,
            Collection<MemberRole> memberRoles
    ) {
        this.id = id;
        this.genderType = genderType;
        this.username = username;
        this.memberRoles = memberRoles;
    }
}