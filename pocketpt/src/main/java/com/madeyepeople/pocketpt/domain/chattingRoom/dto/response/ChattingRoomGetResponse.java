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
public class ChattingRoomGetResponse {
    // 채팅방 정보
    private Long chattingRoomId;
    private String roomName;
    private int notViewCount;
    private LocalDateTime createdAt;

    // 채팅 참여자 정보
    private List<ChattingParticipantResponse> chattingParticipantResponseList;

    // 가장 최근 채팅 정보
    private String latestChattingMessage;
    private String latestFileUrl;
    private LocalDateTime latestChattingMessageCreatedAt;
}
