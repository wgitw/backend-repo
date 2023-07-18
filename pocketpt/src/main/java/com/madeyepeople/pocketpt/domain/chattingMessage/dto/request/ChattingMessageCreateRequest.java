package com.madeyepeople.pocketpt.domain.chattingMessage.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@ToString
public class ChattingMessageCreateRequest {
    private final Long chattingRoomId;

    private final Long chattingParticipantId;

    private final String content;
}
