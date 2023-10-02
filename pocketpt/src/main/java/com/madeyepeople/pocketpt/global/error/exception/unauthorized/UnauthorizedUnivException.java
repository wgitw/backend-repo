package com.madeyepeople.pocketpt.global.error.exception.unauthorized;

import com.madeyepeople.pocketpt.global.error.ErrorCode;

public class UnauthorizedUnivException extends UnauthorizedException {

    public UnauthorizedUnivException() {
        super(ErrorCode.EXAMPLE_USER_ERROR); // TODO: change this
//        super(ErrorCode.UNAUTHORIZED_UNIV);
    }

    public UnauthorizedUnivException(ErrorCode errorCode) {
        super(errorCode);
    }
}
