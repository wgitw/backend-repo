package com.madeyepeople.pocketpt.domain.chattingParticipant.mapper;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import org.springframework.stereotype.Component;

@Component
public class ToChattingParticipantResponse {
    public ChattingParticipantResponse toChattingRoomCreateResponse(ChattingParticipant chattingParticipant, Account account) {
        return ChattingParticipantResponse.builder()
                .chattingParticipantId(chattingParticipant.getChattingParticipantId())
                .accountId(chattingParticipant.getAccountId())
                .profileImageUrl("") // 수정할 것
                .nickname(account.getNickname())
                .isHost(chattingParticipant.getIsHost())
                .createdAt(chattingParticipant.getCreatedAt())
                .build();
    }

    public ChattingParticipantResponse toChattingParticipantCreateResponse(ChattingParticipant chattingParticipant) {
        return ChattingParticipantResponse.builder()
                .chattingParticipantId(chattingParticipant.getChattingParticipantId())
                .accountId(chattingParticipant.getAccountId())
                .isHost(chattingParticipant.getIsHost())
                .createdAt(chattingParticipant.getCreatedAt())
                .build();
    }
}
