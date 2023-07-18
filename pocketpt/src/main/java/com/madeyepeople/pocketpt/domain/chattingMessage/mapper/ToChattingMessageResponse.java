package com.madeyepeople.pocketpt.domain.chattingMessage.mapper;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageCreateResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageGetResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import org.springframework.stereotype.Component;

@Component
public class ToChattingMessageResponse {
    public ChattingMessageCreateResponse toChattingMessageCreateResponse(Long chattingRoomId, Long chattingParticipantId, ChattingMessage chattingMessage) {
        return ChattingMessageCreateResponse.builder()
                .chattingRoomId(chattingRoomId)
                .chattingParticipantId(chattingParticipantId)
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .content(chattingMessage.getContent())
                .isEdited(chattingMessage.getIsEdited())
                .isBookmarked(chattingMessage.getIsBookmarked())
                .createdAt(chattingMessage.getCreatedAt())
                .build();
    }

    public ChattingMessageCreateResponse toChattingFileCreateResponse(Long chattingRoomId, Long chattingParticipantId, ChattingMessage chattingMessage) {
        return ChattingMessageCreateResponse.builder()
                .chattingRoomId(chattingRoomId)
                .chattingParticipantId(chattingParticipantId)
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .fileUrl(chattingMessage.getFileUrl())
                .isEdited(chattingMessage.getIsEdited())
                .isBookmarked(chattingMessage.getIsBookmarked())
                .createdAt(chattingMessage.getCreatedAt())
                .build();
    }

    public ChattingMessageGetResponse toChattingMessageGetResponse(ChattingMessage chattingMessage) {
        return ChattingMessageGetResponse.builder()
                .chattingParticipantId(chattingMessage.getChattingParticipant().getChattingParticipantId())
                .content(chattingMessage.getContent())
                .fileUrl(chattingMessage.getFileUrl())
                .isEdited(chattingMessage.getIsEdited())
                .isBookmarked(chattingMessage.getIsBookmarked())
                .isDeleted(chattingMessage.getIsDeleted())
                .createdAt(chattingMessage.getCreatedAt())
                .updatedAt(chattingMessage.getUpdatedAt())
                .build();
    }

}
