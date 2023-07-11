package com.madeyepeople.pocketpt.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** {행위}_{목적어}_{성공여부} message 는 동사 명사형으로 마무리 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // 도메인 별로 나눠서 관리(ex: User 도메인)
    // group
    GROUP_CREATE_SUCCESS("G001", "그룹 생성 성공"),

    // chatting room
    CHATTING_ROOM_CREATE_SUCCESS("CR001", "채팅방 생성 성공"),

    // file
    FILE_UPLOAD_SUCCESS("F001", "파일 업로드 성공");

    private final String code;
    private final String message;
}
