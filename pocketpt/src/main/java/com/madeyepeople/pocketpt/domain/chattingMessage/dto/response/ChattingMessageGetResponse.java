package com.madeyepeople.pocketpt.domain.chattingMessage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChattingMessageGetResponse {
    private Long chattingRoomId;
    private Long chattingAccountId;
    private String chattingAccountName;
    private String chattingAccountProfilePictureUrl;
    private Long chattingMessageId;
    private String content;
    private String fileUrl;
    private Boolean isEdited;
    private int notViewCount;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
