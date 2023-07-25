package com.madeyepeople.pocketpt.domain.chattingMessage.mapper;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingMessageContentCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import org.springframework.stereotype.Component;

@Component
public class ToChattingMessageEntity {

    public ChattingMessage toChattingMessageCreateEntity(ChattingParticipant chattingParticipant, ChattingMessageContentCreateRequest chattingMessageContentCreateRequest) {
        return ChattingMessage.builder()
                .chattingParticipant(chattingParticipant)
                .content(chattingMessageContentCreateRequest.getContent())
                .build();
    }

    public ChattingMessage toChattingFileCreateEntity(ChattingParticipant chattingParticipant, String fileUrl) {
        return ChattingMessage.builder()
                .chattingParticipant(chattingParticipant)
                .fileUrl(fileUrl)
                .build();
    }
}
