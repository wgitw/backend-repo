package com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChattingParticipantResponse {
    private Long accountId;
    private String accountNickName;
    private String accountProfilePictureUrl;
    private Boolean isHost;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
}
