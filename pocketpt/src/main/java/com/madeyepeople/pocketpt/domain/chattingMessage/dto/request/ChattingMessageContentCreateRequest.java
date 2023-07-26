package com.madeyepeople.pocketpt.domain.chattingMessage.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ChattingMessageContentCreateRequest {
    private String content;
}
