package com.fade.member.vo;

import com.fade.member.constant.MemberRole;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class UserVo extends User {
    private final Long id;
    private final Collection<MemberRole> memberRoles;

    public UserVo(Long id, Collection<MemberRole> roles) {
        super(id + "", "", roles);

        this.memberRoles = roles;
        this.id = id;
    }
}