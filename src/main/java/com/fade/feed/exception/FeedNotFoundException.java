package com.fade.feed.exception;

import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;

public class FeedNotFoundException extends ApplicationException {

    public FeedNotFoundException() {
        super(ErrorCode.NOT_FOUND_FEED);
    }
}
