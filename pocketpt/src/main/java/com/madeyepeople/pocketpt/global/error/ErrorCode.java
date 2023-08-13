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

    // Account
    EXAMPLE_USER_ERROR(HttpStatus.BAD_REQUEST, "E099", "테스트용 예시 에러코드"),
    JWT_VALIDATION_ERROR(HttpStatus.UNAUTHORIZED, "E003", "JWT 유효성 예외 발생"),
    ACCOUNT_ALREADY_REGISTERED(HttpStatus.CONFLICT, "E004", "이미 회원가입된 사용자입니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "E005", "존재하지 않는 사용자"),

    // PtMatching
    PT_MATCHING_ERROR(HttpStatus.NOT_ACCEPTABLE, "E006", "PT matching 관련 오류"),

    // Chatting Room
    CHATTING_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "E007", "존재하지 않는 채팅방"),

    // Chatting Participant
    CHATTING_PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "E008", "존재하지 않는 채팅 참여자"),

    // Chatting Message
    CHATTING_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "E009", "존재하지 않는 채팅 메시지"),

    // Chatting Message Bookmark
    CHATTING_MESSAGE_BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "E010", "존재하지 않는 채팅 메시지 북마크"),

    // File
    CHATTING_FILE_ERROR(HttpStatus.NOT_FOUND, "E010", "파일 관련 오류"),
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