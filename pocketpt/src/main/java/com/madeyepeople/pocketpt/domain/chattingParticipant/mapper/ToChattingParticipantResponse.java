package com.madeyepeople.pocketpt.domain.chattingParticipant.mapper;

import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.request.ChattingParticipantCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantCreateResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.stereotype.Component;

@Component
public class ToChattingParticipantResponse {
    public ChattingParticipantCreateResponse toChattingParticipantCreateResponse(ChattingParticipant chattingParticipant) {
        return ChattingParticipantCreateResponse.builder()
                .chattingParticipantId(chattingParticipant.getChattingParticipantId())
                .participantId(chattingParticipant.getParticipantId())
                .isHost(chattingParticipant.getIsHost())
                .createdAt(chattingParticipant.getCreatedAt())
                .build();
    }
}
