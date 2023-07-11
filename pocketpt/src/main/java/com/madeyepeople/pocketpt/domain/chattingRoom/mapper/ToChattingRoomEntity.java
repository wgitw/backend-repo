package com.madeyepeople.pocketpt.domain.chattingRoom.mapper;

import com.madeyepeople.pocketpt.domain.chattingRoom.dto.request.ChattingRoomCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.stereotype.Component;

@Component
public class ToChattingRoomEntity {
    public ChattingRoom toChattingRoomCreateEntity(ChattingRoomCreateRequest chattingRoomCreateRequest) {
        return ChattingRoom.builder()
                .roomName(Long.toString(chattingRoomCreateRequest.getHostId())) // TODO 추후 유저 이름으로 수정
                .hostId(chattingRoomCreateRequest.getHostId())
                .status(ChattingRoom.ChattingRoomStatus.ACTIVATE)
                .build();
    }
}
