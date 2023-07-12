package com.madeyepeople.pocketpt.domain.chattingRoom.dto.response;

import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ChattingRoomResponse {
    private Long chattingRoomId;
    private String roomName;
    private ChattingRoom.ChattingRoomStatus status;
    private Long hostId;
    private List<ChattingParticipantResponse> chattingParticipantResponseList;
    private LocalDateTime createdAt;
}
