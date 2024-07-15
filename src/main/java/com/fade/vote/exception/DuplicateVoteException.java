package com.fade.vote.exception;

import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;

public class DuplicateVoteException extends ApplicationException {

    public DuplicateVoteException() {
        super(ErrorCode.DUPLICATE_VOTE_ERROR);
    }
}
