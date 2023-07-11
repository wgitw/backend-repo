package com.madeyepeople.pocketpt.global.error.exception;

import com.madeyepeople.pocketpt.global.error.ErrorCode;

public class ExampleException extends BusinessException {
    public ExampleException() {
        super(ErrorCode.EXAMPLE_USER_ERROR);
    }
}
