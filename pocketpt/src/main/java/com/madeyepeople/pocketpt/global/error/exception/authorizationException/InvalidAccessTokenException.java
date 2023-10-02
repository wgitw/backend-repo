package com.madeyepeople.pocketpt.global.error.exception.authorizationException;

import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class InvalidAccessTokenException extends BusinessException {
    public InvalidAccessTokenException(String message) {
        super(ErrorCode.INVALID_TOKEN, message);
    }
}
