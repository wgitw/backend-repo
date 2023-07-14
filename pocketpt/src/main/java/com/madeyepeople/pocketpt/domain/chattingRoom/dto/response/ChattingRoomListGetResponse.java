package com.madeyepeople.pocketpt.domain.chattingRoom.dto.response;

import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ChattingRoomListGetResponse {
    // 채팅방 정보
    private Long chattingRoomId;
    private String roomName;

    // 채팅 참여자 정보
    // TODO: 채팅 참여자 account 테이블 정보로 변경하여 이름과 프로필사진을 response로 넘길 것
    private List<ChattingParticipantResponse> chattingParticipantResponseList;

    // 가장 최근 채팅 정보
    private String latestChattingMessage;
    private String latestFileUrl;
    private LocalDateTime latestChattingMessageCreatedAt;
}
