package com.fade.global.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //COMMON
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN"),

    //TOKEN
    TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    TOKEN_SIGNATURE_ERROR(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 입니다."),
    TOKEN_EXPIRED_ERROR(HttpStatus.UNAUTHORIZED, "토큰이 만료 되었습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "찾을 수 없는 refresh token입니다."),

    //MEMBER
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    ALREADY_EXIST_MEMBER_ID(HttpStatus.CONFLICT, "이미 사용중인 아이디입니다."),
    ALREADY_EXIST_MEMBER(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),
    INVALID_MEMBER_ID_AND_PASSWORD(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호를 확인해주세요."),
    SIGN_IN_WITH_RESIGNED_MEMBER(HttpStatus.FORBIDDEN, "탈퇴한 계정입니다."),

    //FEED
    NOT_FOUND_FEED(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    FEED_UPDATE_DENIED(HttpStatus.FORBIDDEN, "게시글 수정 권한이 없습니다."),
    FEED_DELETE_DENIED(HttpStatus.FORBIDDEN, "게시글 삭제 권한이 없습니다."),

    //VOTE
    DUPLICATE_VOTE_ERROR(HttpStatus.BAD_REQUEST, "중복된 투표입니다."),

    //SOCIAL LOGIN
    NOT_MATCH_SOCIAL_MEMBER(HttpStatus.UNAUTHORIZED, ""),
    NOT_MATCH_OAUTH_CODE(HttpStatus.UNAUTHORIZED, "인증 code가 존재하지 않습니다."),
    NOT_ALLOW_OAUTH_REDIRECT_URI(HttpStatus.BAD_REQUEST, "승인되지 않은 redirectURI입니다."),

    //ATTACHMENT
    ALREADY_EXISTS_ATTACHMENT(HttpStatus.CONFLICT, "이미 동일한 이미지로 업로드된 파일이 존재합니다."),
    NOT_FOUND_ATTACHMENT(HttpStatus.NOT_FOUND, "존재하지 않는 파일입니다."),

    //CATEGORY
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "카테고리 정보를 찾을 수 없습니다."),

    //STYLE
    NOT_FOUND_STYLE(HttpStatus.NOT_FOUND, "스타일 정보를 찾을 수 없습니다."),

    //REPORT
    ALREADY_EXISTS_REPORT(HttpStatus.CONFLICT, "이미 신고한 내역이 있습니다"),

    // BON
    NOT_FOUND_BON(HttpStatus.NOT_FOUND, "찾을 수 없는 BoN입니다."),
    REMOVE_BON_FORBIDDEN(HttpStatus.FORBIDDEN, "BoN을 삭제할 수 있는 권한이 없습니다."),
    ALREADY_EXISTS_BON_VOTE(HttpStatus.CONFLICT, "이미 투표한 BoN입니다."),

    // BON COMMENT
    EXISTS_BON_COMMENT_BY_USER(HttpStatus.CONFLICT, "이미 댓글을 작성한 이력이 있습니다."),
    NOT_FOUND_BON_COMMENT(HttpStatus.NOT_FOUND, "찾을 수 없는 댓글입니다."),
    REMOVE_BON_COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, "댓글을 삭제할 수 있는 권한이 없습니다."),
    CREATE_BON_COMMENT_MUST_BON_VOTE(HttpStatus.CONFLICT, "댓글을 작성하기 위해선 투표를 먼저 해야합니다."),

    // BON COMMENT LIKE
    EXISTS_BON_COMMENT_LIKE(HttpStatus.CONFLICT, "이미 좋아요를 누른 댓글입니다."),

    // BON VOTE
    NOT_FOUND_BON_VOTE(HttpStatus.NOT_FOUND, "투표 내역이 없습니다."),
    REMOVE_BON_VOTE_MUST_REMOVE_COMMENT(HttpStatus.CONFLICT, "투표 내역을 삭제하기 위해서는 댓글을 먼저 삭제해야 합니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
