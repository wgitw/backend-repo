package com.madeyepeople.pocketpt.global.error.exception;

import com.madeyepeople.pocketpt.domain.admin.constant.ServiceLogicConstant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CustomExceptionMessage {
    // global
    AUTHENTICATED_USER_NOT_FOUND("로그인된 사용자가 DB에 존재하지 않습니다"),
    INVALID_DATE_FORMAT("date string 형식이 yyyy-MM-dd가 아닙니다."),

    // sign up
    OAUTH2_NECESSARY_INFO_NOT_FOUND("Provider로부터 필수 제공 정보를 받아오지 못했습니다."),
    PROFILE_IMAGE_PARSING_ERROR("profile image URL 파싱에서 에러가 발생했습니다."),
    TRAINER_MUST_HAVE_MONTHLY_PT_PRICE("트레이너는 월별 PT 단가를 반드시 입력해야 합니다."),
    ACCOUNT_ALREADY_REGISTERED("이미 회원가입된 사용자입니다."),
    ACCOUNT_NOT_FOUND("존재하지 않는 사용자입니다."),

    // account
    CAREER_NOT_FOUND("해당 careerId를 가진 career는 없습니다."),
    CAREER_ACCOUNT_ID_IS_NOT_MATCHED("해당 career의 accountId가 로그인된 사용자의 accountId와 일치하지 않습니다."),
    MONTHLY_PT_PRICE_DUPLICATED_PERIOD("중복되는 개월수(period)가 존재합니다."),
    MONTHLY_PT_PRICE_NOT_FOUND("해당 monthlyPtPriceId를 가진 monthlyPtPrice는 없습니다."),
    MONTHLY_PT_PRICE_ACCOUNT_ID_IS_NOT_MATCHED("해당 monthlyPtPrice의 accountId가 로그인된 사용자의 accountId와 일치하지 않습니다."),
    ACCOUNT_PURPOSE_COUNT_IS_FULL("해당 사용자의 목표 개수가 이미 최대치(" + ServiceLogicConstant.MAXIMUM_NUMBER_OF_PURPOSE + "개)입니다."),

    // pt matching
    TRAINER_ACCOUNT_ID_NOT_FOUND("해당 trainerAccountId를 가진 trainer는 없습니다."),
    TRAINER_IDENTIFICATION_CODE_NOT_FOUND("해당 code를 가진 trainer는 없습니다."),
    IDENTIFICATION_CODE_DUPLICATTED("매우 희박한 확률로 unique 해야할 identification code가 겹쳤습니다. 큰일입니다!!"),
    IDENTIFICATION_CODE_IS_NOT_TRAINER("해당 identification code를 가진 사용자는 trainer가 아닙니다."),
    AUTHENTICATED_USER_IS_NOT_TRAINER("인가된 사용자는 trainer가 아닙니다."),
    PT_MATCHING_NOT_FOUND("해당 PT 매칭 ID가 존재하지 않습니다."),
    PT_MATCHING_STATUS_IS_NOT_PENDING("해당 PT 매칭의 상태가 pending이 아닙니다."),
    PT_MATCHING_TRAINER_ID_IS_NOT_MATCHED("해당 PT 매칭의 trainerId가 로그인된 사용자의 accountId와 일치하지 않습니다. 즉, 다른 trainer의 PT를 수락하려고 하고 있습니다."),
    ACCOUNT_ID_NOT_EXIST_IN_PT_MATCHING("해당 PT 매칭에 해당 accountId가 존재하지 않습니다."),
    PT_MATCHING_REQUEST_ALREADY_EXIST("이미 trainer와 trainee 사이 status = pending인 PT 매칭 요청이 존재합니다."),

    // chatting room
    CHATTING_ROOM_NOT_FOUND("해당 채팅방이 존재하지 않습니다."),
    TOP_CHATTING_ROOM_NOT_FOUND("해당 상단고정 채팅방이 존재하지 않습니다."),
    TOP_CHATTING_ROOM_COUNT_EXCEEDED("상단고정 채팅방 개수를 초과했습니다."),

    // chatting participant
    CHATTING_PARTICIPANT_NOT_FOUND("해당 채팅 참여자가 존재하지 않습니다."),

    // chatting message
    CHATTING_MESSAGE_NOT_FOUND("해당 채팅 메시지가 존재하지 않습니다."),

    // chatting message bookmark
    CHATTING_MESSAGE_BOOKMARK_NOT_FOUND("해당 채팅 메시지 북마크가 존재하지 않습니다."),

    // file
    CHATTING_FILE_UPLOAD_FAILED("해당 파일을 업로드할 수 없습니다."),
    CHATTING_FILE_DOWNLOAD_FAILED("해당 파일을 다운로드할 수 없습니다."),
    ;

    private final String message;
}
