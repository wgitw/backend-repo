package com.madeyepeople.pocketpt.domain.chattingMessage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class ChattingMessageCreateResponse {
    private Long chattingRoomId;
    private Long chattingAccountId;
    private String chattingAccountName;
    private String chattingAccountProfilePictureUrl;
    private Long chattingMessageId;
    private String content;
    private String fileUrl;
    private Boolean isEdited;
    private int notViewCount;
    private LocalDateTime createdAt;
}
