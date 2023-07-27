package com.madeyepeople.pocketpt.domain.chattingParticipant.mapper;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import org.springframework.stereotype.Component;

@Component
public class ToChattingParticipantResponse {
    public ChattingParticipantResponse toChattingRoomCreateResponse(ChattingParticipant chattingParticipant) {
        return ChattingParticipantResponse.builder()
                .accountId(chattingParticipant.getAccount().getAccountId())
                .accountNickName(chattingParticipant.getAccount().getNickname())
                .accountProfilePictureUrl(chattingParticipant.getAccount().getProfilePictureUrl())
                .isHost(chattingParticipant.getIsHost())
                .createdAt(chattingParticipant.getCreatedAt())
                .build();
    }

    public ChattingParticipantResponse toChattingParticipantCreateResponse(ChattingParticipant chattingParticipant) {
        return ChattingParticipantResponse.builder()
                .accountId(chattingParticipant.getAccount().getAccountId())
                .accountNickName(chattingParticipant.getAccount().getNickname())
                .accountProfilePictureUrl(chattingParticipant.getAccount().getProfilePictureUrl())
                .isHost(chattingParticipant.getIsHost())
                .createdAt(chattingParticipant.getCreatedAt())
                .build();
    }
}
