package com.madeyepeople.pocketpt.domain.chattingRoom.mapper;

import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantCreateResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.request.ChattingRoomCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomCreateResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ToChattingRoomResponse {

    public ChattingRoomCreateResponse toChattingRoomCreateResponse(ChattingRoom chattingRoom, List<ChattingParticipantCreateResponse> chattingParticipantCreateResponseList) {
        return ChattingRoomCreateResponse.builder()
                .chattingRoomId(chattingRoom.getChattingRoomId())
                .roomName(chattingRoom.getRoomName())
                .status(chattingRoom.getStatus())
                .hostId(chattingRoom.getHostId())
                .chattingParticipantCreateResponseList(chattingParticipantCreateResponseList)
                .createdAt(chattingRoom.getCreatedAt())
                .build();
    }

}
