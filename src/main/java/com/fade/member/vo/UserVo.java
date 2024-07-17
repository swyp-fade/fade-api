package com.fade.member.vo;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class UserVo extends User {
    private final Long id;

    public UserVo(Long id) {
        super(id + "", null, Collections.emptyList());

        this.id = id;
    }
}