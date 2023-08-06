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

    // ptMatching
    PT_REGISTRATION_CREATE_SUCCESS("PM001", "PT 요청 성공"),
    PT_MATCHING_LIST_GET_SUCCESS("PM002", "PT 매칭 리스트 가져오기 성공"),

    // group
    GROUP_CREATE_SUCCESS("G001", "그룹 생성 성공"),

    // chatting room
    CHATTING_ROOM_CREATE_SUCCESS("CR001", "채팅방 생성 성공"),
    CHATTING_ROOM_LIST_GET_SUCCESS("CR002", "채팅방 리스트 가져오기 성공"),

    // chatting message
    CHATTING_MESSAGE_CREATE_SUCCESS("CM001", "채팅 메시지 생성 성공"),
    CHATTING_FILE_CREATE_SUCCESS("CM002", "채팅 파일 생성 성공"),
    CHATTING_MESSAGE_LIST_GET_SUCCESS("CM003", "채팅 메시지 리스트 가져오기 성공"),
    CHATTING_FILE_LIST_GET_SUCCESS("CM004", "채팅 파일 리스트 가져오기 성공"),

    // file
    FILE_UPLOAD_SUCCESS("F001", "파일 업로드 성공"),

    // chatting message bookmark
    CHATTING_MESSAGE_BOOKMARK_CREATE_SUCCESS("CB001", "채팅 북마크 성공"),
    CHATTING_MESSAGE_BOOKMARK_LIST_GET_SUCCESS("CB002", "채팅 북마크 리스트 가져오기 성공"),
    CHATTING_MESSAGE_BOOKMARK_DELETE_SUCCESS("CB003", "채팅 북마크 삭제 성공");

    private final String code;
    private final String message;
}
