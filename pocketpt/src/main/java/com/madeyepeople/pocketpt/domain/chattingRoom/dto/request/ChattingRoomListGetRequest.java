package com.madeyepeople.pocketpt.domain.chattingRoom.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ChattingRoomListGetRequest {
    private Long userId;
}
