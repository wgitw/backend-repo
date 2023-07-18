package com.madeyepeople.pocketpt.domain.chattingMessage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChattingMessageGetResponse {
    private Long chattingParticipantId;
    private String content;
    private String fileUrl;
    private Boolean isEdited;
    private Boolean isBookmarked;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
