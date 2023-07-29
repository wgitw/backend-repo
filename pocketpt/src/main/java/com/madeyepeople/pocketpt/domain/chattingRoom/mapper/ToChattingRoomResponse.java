package com.madeyepeople.pocketpt.domain.chattingRoom.mapper;

import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomGetResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomListPaginationResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ToChattingRoomResponse {

    public ChattingRoomResponse toChattingRoomCreateResponse(ChattingRoom chattingRoom,
                                                             List<ChattingParticipantResponse> chattingParticipantResponseList,
                                                             String participantName) {
        return ChattingRoomResponse.builder()
                .chattingRoomId(chattingRoom.getChattingRoomId())
                .roomName(participantName)
                .status(chattingRoom.getStatus())
                .hostId(chattingRoom.getHostId())
                .chattingParticipantResponseList(chattingParticipantResponseList)
                .createdAt(chattingRoom.getCreatedAt())
                .build();
    }

    public ChattingRoomGetResponse toChattingRoomListGetResponse(ChattingRoom chattingRoom,
                                                                 String roomName,
                                                                 int notViewCount,
                                                                 List<ChattingParticipantResponse> chattingParticipantResponseList,
                                                                 ChattingMessage chattingMessage) {
        return ChattingRoomGetResponse.builder()
                .chattingRoomId(chattingRoom.getChattingRoomId())
                .notViewCount(notViewCount)
                .roomName(roomName)
                .createdAt(chattingRoom.getCreatedAt())
                .chattingParticipantResponseList(chattingParticipantResponseList)
                .latestChattingMessage(chattingMessage.getContent())
                .latestFileUrl(chattingMessage.getFileUrl())
                .latestChattingMessageCreatedAt(chattingMessage.getCreatedAt())
                .build();
    }

    public static ChattingRoomListPaginationResponse toChattingRoomListPaginationResponse(Slice<ChattingParticipant> chattingParticipantList,
                                                                                          List<ChattingRoomGetResponse> chattingRoomResponseList) {
        return ChattingRoomListPaginationResponse.builder()
                .chattingRoomResponseList(chattingRoomResponseList)
                .pageable(chattingParticipantList.getPageable())
                .number(chattingParticipantList.getNumber())
                .numberOfElements(chattingParticipantList.getNumberOfElements())
                .first(chattingParticipantList.isFirst())
                .last(chattingParticipantList.isLast())
                .empty(chattingParticipantList.isEmpty())
                .build();
    }

}
