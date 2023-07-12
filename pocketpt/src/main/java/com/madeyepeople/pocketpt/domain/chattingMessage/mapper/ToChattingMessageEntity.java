package com.madeyepeople.pocketpt.domain.chattingMessage.mapper;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingMessageCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import org.springframework.stereotype.Component;

@Component
public class ToChattingMessageEntity {

    public ChattingMessage toChattingMessageCreateEntity(ChattingParticipant chattingParticipant, ChattingMessageCreateRequest chattingMessageCreateRequest) {
        return ChattingMessage.builder()
                .chattingParticipant(chattingParticipant)
                .content(chattingMessageCreateRequest.getContent())
                .build();
    }

    public ChattingMessage toChattingFileCreateEntity(ChattingParticipant chattingParticipant, ChattingMessageCreateRequest chattingMessageCreateRequest, String fileUrl) {
        return ChattingMessage.builder()
                .chattingParticipant(chattingParticipant)
                .fileUrl(fileUrl)
                .build();
    }
}
