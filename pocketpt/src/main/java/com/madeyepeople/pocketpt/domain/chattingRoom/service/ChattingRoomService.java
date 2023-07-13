package com.madeyepeople.pocketpt.domain.chattingRoom.service;

import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.request.ChattingParticipantCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingParticipant.mapper.ToChattingParticipantEntity;
import com.madeyepeople.pocketpt.domain.chattingParticipant.mapper.ToChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.repository.ChattingParticipantRepository;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.request.ChattingRoomCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.domain.chattingRoom.mapper.ToChattingRoomEntity;
import com.madeyepeople.pocketpt.domain.chattingRoom.mapper.ToChattingRoomResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.repository.ChattingRoomRepository;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingRoomService {
    private final ChattingRoomRepository chattingRoomRepository;

    private final ChattingParticipantRepository chattingParticipantRepository;

    private final ToChattingRoomEntity toChattingRoomEntity;

    private final ToChattingRoomResponse toChattingRoomResponse;

    private final ToChattingParticipantEntity toChattingParticipantEntity;

    private final ToChattingParticipantResponse toChattingParticipantResponse;

    @Transactional
    public ResultResponse createChattingRoom(ChattingRoomCreateRequest chattingRoomCreateRequest) {
        ChattingRoom chattingRoom = toChattingRoomEntity.toChattingRoomCreateEntity(chattingRoomCreateRequest);
        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);

        List<ChattingParticipantResponse> chattingParticipantResponseList = new ArrayList<>();

        for(ChattingParticipantCreateRequest chattingParticipantCreateRequest: chattingRoomCreateRequest.getChattingParticipantCreateRequestList()) {
            ChattingParticipant chattingParticipant = toChattingParticipantEntity.toChattingParticipantCreateEntity(savedChattingRoom, chattingParticipantCreateRequest);
            ChattingParticipant savedChattingParticipant = chattingParticipantRepository.save(chattingParticipant);
            ChattingParticipantResponse chattingParticipantResponse = toChattingParticipantResponse.toChattingParticipantCreateResponse(savedChattingParticipant);
            chattingParticipantResponseList.add(chattingParticipantResponse);
        }
        ChattingRoomResponse chattingRoomResponse = toChattingRoomResponse.toChattingRoomCreateResponse(savedChattingRoom, chattingParticipantResponseList);

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_CREATE_SUCCESS, chattingRoomResponse);

        return resultResponse;
    }

}
