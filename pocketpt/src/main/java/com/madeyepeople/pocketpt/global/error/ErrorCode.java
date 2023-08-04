package com.madeyepeople.pocketpt.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "서버 오류"),
    INPUT_INVALID_VALUE(HttpStatus.NOT_ACCEPTABLE, "E002", "잘못된 입력"),

    // Account 도메인
    EXAMPLE_USER_ERROR(HttpStatus.BAD_REQUEST, "E099", "테스트용 예시 에러코드"),
    JWT_VALIDATION_ERROR(HttpStatus.UNAUTHORIZED, "E003", "JWT 유효성 예외 발생"),

    // PtMatching 도메인
    TRAINER_IDENTIFICATION_CODE_NOT_FOUND(HttpStatus.NOT_ACCEPTABLE, "PT001", "Trainer code 오류"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}