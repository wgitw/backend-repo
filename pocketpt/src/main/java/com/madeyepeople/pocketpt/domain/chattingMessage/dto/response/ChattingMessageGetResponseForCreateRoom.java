package com.madeyepeople.pocketpt.domain.chattingMessage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChattingMessageGetResponseForCreateRoom {
    private Long chattingRoomId;
    private Long hostChattingAccountId;
    private String hostChattingAccountName;
    private String hostChattingAccountProfilePictureUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
