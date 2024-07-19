package com.fade.global.exception;

import com.fade.global.constant.ErrorCode;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class ApplicationException extends RuntimeException{

    private final ErrorCode errorCode;
    private final Map<String, Object> data;

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = Collections.emptyMap();
    }

    public ApplicationException(ErrorCode errorCode, Map<String, Object> data) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = data;
    }
}
