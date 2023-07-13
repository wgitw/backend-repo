package com.madeyepeople.pocketpt.domain.chattingMessage.mapper;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import org.springframework.stereotype.Component;

@Component
public class ToChattingMessageResponse {
    public ChattingMessageResponse toChattingMessageCreateResponse(Long chattingRoomId, Long chattingParticipantId, ChattingMessage chattingMessage) {
        return ChattingMessageResponse.builder()
                .chattingRoomId(chattingRoomId)
                .chattingParticipantId(chattingParticipantId)
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .content(chattingMessage.getContent())
                .isEdited(chattingMessage.getIsEdited())
                .isBookmarked(chattingMessage.getIsBookmarked())
                .createdAt(chattingMessage.getCreatedAt())
                .build();
    }

    public ChattingMessageResponse toChattingFileCreateResponse(Long chattingRoomId, Long chattingParticipantId, ChattingMessage chattingMessage) {
        return ChattingMessageResponse.builder()
                .chattingRoomId(chattingRoomId)
                .chattingParticipantId(chattingParticipantId)
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .fileUrl(chattingMessage.getFileUrl())
                .isEdited(chattingMessage.getIsEdited())
                .isBookmarked(chattingMessage.getIsBookmarked())
                .createdAt(chattingMessage.getCreatedAt())
                .build();
    }

}
