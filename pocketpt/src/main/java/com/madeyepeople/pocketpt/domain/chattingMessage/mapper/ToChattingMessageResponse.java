package com.madeyepeople.pocketpt.domain.chattingMessage.mapper;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageCreateResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageGetResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import org.springframework.stereotype.Component;

@Component
public class ToChattingMessageResponse {
    public ChattingMessageCreateResponse toChattingMessageCreateResponse(ChattingMessage chattingMessage) {
        return ChattingMessageCreateResponse.builder()
                .chattingRoomId(chattingMessage.getChattingParticipant().getChattingRoom().getChattingRoomId())
                .chattingAccountId(chattingMessage.getChattingParticipant().getAccount().getAccountId())
                .chattingAccountName(chattingMessage.getChattingParticipant().getAccount().getNickname())
                .chattingAccountProfilePictureUrl(chattingMessage.getChattingParticipant().getAccount().getProfilePictureUrl())
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .content(chattingMessage.getContent())
                .isEdited(chattingMessage.getIsEdited())
                .isBookmarked(chattingMessage.getIsBookmarked())
                .notViewCount(chattingMessage.getNotViewCount())
                .createdAt(chattingMessage.getCreatedAt())
                .build();
    }

    public ChattingMessageCreateResponse toChattingFileCreateResponse(ChattingMessage chattingMessage) {
        return ChattingMessageCreateResponse.builder()
                .chattingRoomId(chattingMessage.getChattingParticipant().getChattingRoom().getChattingRoomId())
                .chattingAccountId(chattingMessage.getChattingParticipant().getAccount().getAccountId())
                .chattingAccountName(chattingMessage.getChattingParticipant().getAccount().getNickname())
                .chattingAccountProfilePictureUrl(chattingMessage.getChattingParticipant().getAccount().getProfilePictureUrl())
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .fileUrl(chattingMessage.getFileUrl())
                .isEdited(chattingMessage.getIsEdited())
                .isBookmarked(chattingMessage.getIsBookmarked())
                .notViewCount(chattingMessage.getNotViewCount())
                .createdAt(chattingMessage.getCreatedAt())
                .build();
    }

    public ChattingMessageGetResponse toChattingMessageGetResponse(ChattingMessage chattingMessage) {
        return ChattingMessageGetResponse.builder()
                .chattingRoomId(chattingMessage.getChattingParticipant().getChattingRoom().getChattingRoomId())
                .chattingAccountId(chattingMessage.getChattingParticipant().getAccount().getAccountId())
                .chattingAccountName(chattingMessage.getChattingParticipant().getAccount().getNickname())
                .chattingAccountProfilePictureUrl(chattingMessage.getChattingParticipant().getAccount().getProfilePictureUrl())
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .content(chattingMessage.getContent())
                .fileUrl(chattingMessage.getFileUrl())
                .isEdited(chattingMessage.getIsEdited())
                .isBookmarked(chattingMessage.getIsBookmarked())
                .notViewCount(chattingMessage.getNotViewCount())
                .isDeleted(chattingMessage.getIsDeleted())
                .createdAt(chattingMessage.getCreatedAt())
                .updatedAt(chattingMessage.getUpdatedAt())
                .build();
    }

}
