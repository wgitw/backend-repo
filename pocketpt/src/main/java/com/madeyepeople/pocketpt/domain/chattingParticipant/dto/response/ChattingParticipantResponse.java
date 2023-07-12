package com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChattingParticipantResponse {
    private Long chattingParticipantId;
    private Long participantId;
    private Boolean isHost;
    private LocalDateTime createdAt;
}
