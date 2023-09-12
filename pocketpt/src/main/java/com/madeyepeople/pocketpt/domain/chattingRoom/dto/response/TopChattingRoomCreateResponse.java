package com.madeyepeople.pocketpt.domain.chattingRoom.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TopChattingRoomCreateResponse {
    private Long chattingRoomId;
    private Long accountId;
    private LocalDateTime createdAt;
}
