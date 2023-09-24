package com.madeyepeople.pocketpt.global.error.exception.authorizationException;

import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class AccountNotExistException extends BusinessException {
    public AccountNotExistException(String message) {
        super(ErrorCode.ACCOUNT_NOT_FOUND, message);
    }
}
