package com.madeyepeople.pocketpt.global.error.exception.authorizationException;

import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class DiscardedTokenException extends BusinessException {
    public DiscardedTokenException() {
        super(ErrorCode.DISCARDED_TOKEN);
    }
}
