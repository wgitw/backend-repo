package com.madeyepeople.pocketpt.domain.chattingParticipant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@ToString
public class ChattingParticipantCreateRequest {
//    private final ChattingRoom chattingRoom; // 기본 채팅방에 멤버를 추가하는 경우만 필요할 듯
    private final Long participantId;
    private final Boolean isHost;
}
