package com.madeyepeople.pocketpt.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** {행위}_{목적어}_{성공여부} message 는 동사 명사형으로 마무리 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // 도메인 별로 나눠서 관리(ex: User 도메인)
    // account
    ACCOUNT_CREATE_SUCCESS("A001", "회원 가입 성공"),
    ACCOUNT_GET_SUCCESS("A002", "특정 회원 상세 조회 성공"),
    ACCOUNT_CHECK_SIGNED_UP_SUCCESS("A003", "1차 회원 가입 여부 조회 성공"),
    ACCOUNT_PT_PRICE_GET_SUCCESS("A004", "트레이너의 월간 PT 가격 조회 성공"),
    ACCOUNT_TOTAL_SALES_GET_SUCCESS("A005", "트레이너의 총 매출 조회 성공"),
    ACCOUNT_MONTHLY_SALES_GET_SUCCESS("A006", "트레이너의 월별 매출 조회 성공"),
    ACCOUNT_TRAINER_CAREER_CREATE_SUCCESS("A007", "트레이너의 이력 생성 성공"),
    ACCOUNT_TRAINER_CAREER_GET_SUCCESS("A008", "트레이너의 이력 조회 성공"),
    ACCOUNT_TRAINER_CAREER_UPDATE_SUCCESS("A009", "트레이너의 이력 수정 성공"),
    ACCOUNT_TRAINER_CAREER_DELETE_SUCCESS("A010", "트레이너의 이력 삭제 성공"),
    ACCOUNT_INCOME_GET_SUCCESS("A011", "매출에 대한 순수익 조회 성공"),
    ACCOUNT_TRAINER_MONTHLY_PT_PRICE_CREATE_SUCCESS("A012", "트레이너의 월별 단가 생성 성공"),
    ACCOUNT_TRAINER_MONTHLY_PT_PRICE_UPDATE_SUCCESS("A013", "트레이너의 월별 단가 수정 성공"),
    ACCOUNT_TRAINER_MONTHLY_PT_PRICE_DELETE_SUCCESS("A014", "트레이너의 월별 단가 삭제 성공"),
    ACCOUNT_PURPOSE_CREATE_SUCCESS("A015", "목표 생성 성공"),
    ACCOUNT_PURPOSE_GET_SUCCESS("A016", "목표 조회 성공"),
    ACCOUNT_PURPOSE_UPDATE_SUCCESS("A017", "목표 수정 성공"),
    ACCOUNT_PURPOSE_DELETE_SUCCESS("A018", "목표 삭제 성공"),
    ACCOUNT_PROFILE_GET_SUCCESS("A019", "프로필 조회 성공"),

    // ptMatching
    PT_REGISTRATION_CREATE_SUCCESS("PM001", "PT 요청 성공"),
    PT_MATCHING_LIST_GET_SUCCESS("PM002", "PT 매칭 리스트 가져오기 성공"),
    PT_MATCHING_ACCEPT_SUCCESS("PM003", "PT 매칭 수락 성공"),
    PT_MATCHING_PAYMENT_AMOUNT_GET_SUCCESS("PM004", "예상 결제 금액 조회 성공"),
    PT_MATCHING_REJECT_SUCCESS("PM005", "요청된 PT 거절 성공"),

    // group
    GROUP_CREATE_SUCCESS("G001", "그룹 생성 성공"),

    // chatting room
    CHATTING_ROOM_CREATE_SUCCESS("CR001", "채팅방 생성 성공"),
    CHATTING_ROOM_LIST_GET_SUCCESS("CR002", "채팅방 리스트 가져오기 성공"),
    CHATTING_ROOM_LIST_UPDATE_INFO_FOR_MESSAGE_GET_SUCCESS("CR003", "채팅방 리스트 -새로운 메시지- 변경사항 전송 성공"),
    CHATTING_ROOM_LIST_UPDATE_INFO_FOR_ROOM_GET_SUCCESS("CR004", "채팅방 리스트 -새로운 채팅방 생성- 변경사항 전송 성공"),
    CHATTING_ROOM_ENTER_SUCCESS("CR005", "채팅방 입장 성공"),
    CHATTING_ROOM_EXIT_SUCCESS("CR006", "채팅방 퇴장 성공"),
    CHATTING_ROOM_DELETE_SUCCESS("CR007", "채팅방 삭제 성공"),
    TOP_CHATTING_ROOM_CREATE_SUCCESS("CR008", "채팅방 상단고정 성공"),
    TOP_CHATTING_ROOM_DELETE_SUCCESS("CR009", "채팅방 상단고정 해제 성공"),
    TOP_CHATTING_ROOM_LIST_GET_SUCCESS("CR010", "채팅방 상단고정 리스트 가져오기 성공"),

    // chatting message
    CHATTING_MESSAGE_CREATE_SUCCESS("CM001", "채팅 메시지 생성 성공"),
    CHATTING_FILE_CREATE_SUCCESS("CM002", "채팅 파일 생성 성공"),
    CHATTING_MESSAGE_LIST_GET_SUCCESS("CM003", "채팅 메시지 리스트 가져오기 성공"),
    CHATTING_FILE_LIST_GET_SUCCESS("CM004", "채팅 파일 리스트 가져오기 성공"),
    CHATTING_MESSAGE_UPDATE_SUCCESS("CM005", "채팅 메시지 수정 성공"),
    CHATTING_MESSAGE_DELETE_SUCCESS("CM006", "채팅 메시지 삭제 성공"),

    // file
    FILE_UPLOAD_SUCCESS("F001", "파일 업로드 성공"),

    // chatting message bookmark
    CHATTING_MESSAGE_BOOKMARK_CREATE_SUCCESS("CB001", "채팅 북마크 성공"),
    CHATTING_MESSAGE_BOOKMARK_LIST_GET_SUCCESS("CB002", "채팅 북마크 리스트 가져오기 성공"),
    CHATTING_MESSAGE_BOOKMARK_DELETE_SUCCESS("CB003", "채팅 북마크 삭제 성공"),

    // historical data
    HISTORICAL_DATA_CREATE_SUCCESS("HD001", "historical data 생성 성공"),
    HISTORICAL_DATA_GET_SUCCESS("HD002", "historical data 조회 성공"),
    HISTORICAL_DATA_UPDATE_SUCCESS("HD003", "historical data 수정 성공"),
    HISTORICAL_DATA_DELETE_SUCCESS("HD004", "historical data 삭제 성공"),

    // historical data file
    HISTORICAL_DATA_FILE_CREATE_SUCCESS("HDF001", "historical data file 생성 성공"),
    HISTORICAL_DATA_FILE_GET_SUCCESS("HDF002", "historical data file 조회 성공"),
    HISTORICAL_DATA_FILE_UPDATE_SUCCESS("HDF003", "historical data file 수정 성공"),
    HISTORICAL_DATA_FILE_DELETE_SUCCESS("HDF004", "historical data file 삭제 성공"),
    ;

    private final String code;
    private final String message;
}
