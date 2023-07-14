package com.madeyepeople.pocketpt.domain.chattingRoom.mapper;

import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomListGetResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ToChattingRoomResponse {

    public ChattingRoomResponse toChattingRoomCreateResponse(ChattingRoom chattingRoom,
                                                             List<ChattingParticipantResponse> chattingParticipantResponseList) {
        return ChattingRoomResponse.builder()
                .chattingRoomId(chattingRoom.getChattingRoomId())
                .roomName(chattingRoom.getRoomName())
                .status(chattingRoom.getStatus())
                .hostId(chattingRoom.getHostId())
                .chattingParticipantResponseList(chattingParticipantResponseList)
                .createdAt(chattingRoom.getCreatedAt())
                .build();
    }

    public ChattingRoomListGetResponse toChattingRoomListGetResponse(ChattingRoom chattingRoom,
                                                                     List<ChattingParticipantResponse> chattingParticipantResponseList,
                                                                     ChattingMessage chattingMessage) {
        return ChattingRoomListGetResponse.builder()
                .chattingRoomId(chattingRoom.getChattingRoomId())
                .roomName(chattingRoom.getRoomName())
                .chattingParticipantResponseList(chattingParticipantResponseList)
                .latestChattingMessage(chattingMessage.getContent())
                .latestFileUrl(chattingMessage.getFileUrl())
                .latestChattingMessageCreatedAt(chattingMessage.getCreatedAt())
                .build();
    }

}
