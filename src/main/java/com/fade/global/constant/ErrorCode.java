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
    ALREADY_EXIST_MEMBER(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),
    INVALID_MEMBER_ID_AND_PASSWORD(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호를 확인해주세요."),

    //FEED
    NOT_FOUND_FEED(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    FEED_UPDATE_DENIED(HttpStatus.FORBIDDEN, "게시글 수정 권한이 없습니다."),
    FEED_DELETE_DENIED(HttpStatus.FORBIDDEN, "게시글 삭제 권한이 없습니다."),

    //VOTE
    DUPLICATE_VOTE_ERROR(HttpStatus.BAD_REQUEST, "중복된 투표입니다."),

    //SOCIAL LOGIN
    NOT_MATCH_SOCIAL_MEMBER(HttpStatus.UNAUTHORIZED, ""),

    //ATTACHMENT
    ALREADY_EXISTS_ATTACHMENT(HttpStatus.CONFLICT, "이미 동일한 이미지로 업로드된 파일이 존재합니다."),
    NOT_FOUND_ATTACHMENT(HttpStatus.NOT_FOUND, "존재하지 않는 파일입니다."),

    //CATEGORY
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "카테고리 정보를 찾을 수 없습니다."),

    //STYLE
    NOT_FOUND_STYLE(HttpStatus.NOT_FOUND, "스타일 정보를 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
