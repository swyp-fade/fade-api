package com.fade.global.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //TOKEN
    TOKEN_SIGNATURE_ERROR(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 입니다."),
    TOKEN_EXPIRED_ERROR(HttpStatus.UNAUTHORIZED, "토큰이 만료 되었습니다.."),
    TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),

    //MEMBER
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    ALREADY_EXIST_MEMBER_ID(HttpStatus.CONFLICT, "이미 사용중인 아이디입니다."),
    INVALID_MEMBER_ID_AND_PASSWORD(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호를 확인해주세요."),

    //POST
    POST_UPDATE_DENIED(HttpStatus.FORBIDDEN, "게시글 수정 권한이 없습니다."),
    POST_DELETE_DENIED(HttpStatus.FORBIDDEN, "게시글 삭제 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
