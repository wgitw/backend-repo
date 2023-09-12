package com.madeyepeople.pocketpt.domain.chattingRoom.mapper;

import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.TopChattingRoomCreateResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.TopChattingRoom;
import org.springframework.stereotype.Component;

@Component
public class ToTopChattingRoomResponse {
    public TopChattingRoomCreateResponse toTopChattingRoomCreateResponse(TopChattingRoom topChattingRoom) {
        return TopChattingRoomCreateResponse.builder()
                .chattingRoomId(topChattingRoom.getChattingRoom().getChattingRoomId())
                .accountId(topChattingRoom.getAccount().getAccountId())
                .createdAt(topChattingRoom.getCreatedAt())
                .build();
    }
}
