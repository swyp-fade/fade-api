package com.fade.vote.exception;

import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;

public class DuplicateVoteException extends ApplicationException {

    public DuplicateVoteException(ErrorCode errorCode) {
        super(errorCode);
    }
}
