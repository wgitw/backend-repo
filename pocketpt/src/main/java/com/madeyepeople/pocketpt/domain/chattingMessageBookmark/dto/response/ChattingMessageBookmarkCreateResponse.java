package com.madeyepeople.pocketpt.domain.chattingMessageBookmark.dto.response;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageGetResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChattingMessageBookmarkCreateResponse {
    private Long chattingRoomId;
    private Long chattingMessageId;
    private ChattingMessageGetResponse chattingMessageGetResponse;
    private LocalDateTime createdAt;
}
