package com.fade.global.exception;

import com.fade.global.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException{

    private final ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
