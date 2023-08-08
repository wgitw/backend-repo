package com.madeyepeople.pocketpt.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CustomExceptionMessage {
    // global
    AUTHENTICATED_USER_NOT_FOUND("로그인된 사용자가 DB에 존재하지 않습니다"),

    // sign up
    OAUTH2_NECESSARY_INFO_NOT_FOUND("Provider로부터 필수 제공 정보를 받아오지 못했습니다."),
    PROFILE_IMAGE_PARSING_ERROR("profile image URL 파싱에서 에러가 발생했습니다."),

    // pt matching
    TRAINER_IDENTIFICATION_CODE_NOT_FOUND("해당 code를 가진 trainer는 없습니다."),
    IDENTIFICATION_CODE_DUPLICATTED("매우 희박한 확률로 unique 해야할 identification code가 겹쳤습니다. 큰일입니다!!"),
    IDENTIFICATION_CODE_IS_NOT_TRAINER("해당 identification code를 가진 사용자는 trainer가 아닙니다."),
    AUTHENTICATED_USER_IS_NOT_TRAINER("인가된 사용자는 trainer가 아닙니다."),
    PT_MATCHING_NOT_FOUND("해당 PT 매칭 ID가 존재하지 않습니다."),
    PT_MATCHING_STATUS_IS_NOT_PENDING("해당 PT 매칭의 상태가 pending이 아닙니다."),
    PT_MATCHING_TRAINER_ID_IS_NOT_MATCHED("해당 PT 매칭의 trainerId가 로그인된 사용자의 accountId와 일치하지 않습니다. 즉, 다른 trainer의 PT를 수락하려고 하고 있습니다."),
    ACCOUNT_ID_NOT_EXIST_IN_PT_MATCHING("해당 PT 매칭에 해당 accountId가 존재하지 않습니다."),


    ;

    private final String message;
}
