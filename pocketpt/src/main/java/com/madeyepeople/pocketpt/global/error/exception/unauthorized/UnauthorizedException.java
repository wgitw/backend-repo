package com.madeyepeople.pocketpt.global.error.exception.unauthorized;

import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;

/*
 * 리소스에 대한 접근 권한이 없을 때 발생
 */
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
