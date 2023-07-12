package com.madeyepeople.pocketpt.domain.chattingMessage.dto.response;

import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChattingMessageResponse {
    private Long chattingRoomId;
    private Long chattingParticipantId;
    private Long chattingMessageId;
    private String content;
    private String fileUrl;
    private Boolean isEdited;
    private Boolean isBookmarked;
    private LocalDateTime createdAt;
}
