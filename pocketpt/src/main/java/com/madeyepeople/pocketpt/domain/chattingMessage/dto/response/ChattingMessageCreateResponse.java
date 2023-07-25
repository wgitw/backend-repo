package com.madeyepeople.pocketpt.domain.chattingMessage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChattingMessageCreateResponse {
    private Long chattingRoomId;
    private Long chattingParticipantId; // TODO: account 이름으로 변경할 것
    private Long chattingMessageId;
    private String content;
    private String fileUrl;
    private Boolean isEdited;
    private Boolean isBookmarked;
    private int notViewCount;
    private LocalDateTime createdAt;
}
