package com.madeyepeople.pocketpt.domain.chattingRoom.mapper;

import com.madeyepeople.pocketpt.domain.chattingRoom.dto.request.ChattingRoomCreateByParticipantListRequest;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.stereotype.Component;

@Component
public class ToChattingRoomEntity {
    public ChattingRoom toChattingRoomCreateByParticipantListEntity(ChattingRoomCreateByParticipantListRequest chattingRoomCreateByParticipantListRequest) {
        return ChattingRoom.builder()
                .roomName(Long.toString(chattingRoomCreateByParticipantListRequest.getHostId())) // TODO 추후 유저 이름으로 수정
                .hostId(chattingRoomCreateByParticipantListRequest.getHostId())
                .status(ChattingRoom.ChattingRoomStatus.ACTIVATE)
                .build();
    }

    public ChattingRoom toChattingRoomCreateEntity(Long hostParticipant) {
        return ChattingRoom.builder()
                .hostId(hostParticipant)
                .status(ChattingRoom.ChattingRoomStatus.ACTIVATE)
                .numberOfParticipant(2) // 추후 여러 멤버로 들어올 경우 변경할 것
                .build();
    }
}
