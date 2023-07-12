package com.madeyepeople.pocketpt.domain.chattingParticipant.mapper;

import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import org.springframework.stereotype.Component;

@Component
public class ToChattingParticipantResponse {
    public ChattingParticipantResponse toChattingParticipantCreateResponse(ChattingParticipant chattingParticipant) {
        return ChattingParticipantResponse.builder()
                .chattingParticipantId(chattingParticipant.getChattingParticipantId())
                .participantId(chattingParticipant.getParticipantId())
                .isHost(chattingParticipant.getIsHost())
                .createdAt(chattingParticipant.getCreatedAt())
                .build();
    }
}
