package com.madeyepeople.pocketpt.domain.chattingRoom.service;

import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.request.ChattingParticipantCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantCreateResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingParticipant.mapper.ToChattingParticipantEntity;
import com.madeyepeople.pocketpt.domain.chattingParticipant.mapper.ToChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.repository.ChattingParticipantRepository;
import com.madeyepeople.pocketpt.domain.chattingRoom.controller.ChattingRoomController;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.request.ChattingRoomCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomCreateResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.domain.chattingRoom.mapper.ToChattingRoomEntity;
import com.madeyepeople.pocketpt.domain.chattingRoom.mapper.ToChattingRoomResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.repository.ChattingRoomRepository;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingRoomService {

    @Autowired
    private final ChattingRoomRepository chattingRoomRepository;

    @Autowired
    private final ChattingParticipantRepository chattingParticipantRepository;

    @Autowired
    private final ToChattingRoomEntity toChattingRoomEntity;
    @Autowired
    private final ToChattingRoomResponse toChattingRoomResponse;

    @Autowired
    private final ToChattingParticipantEntity toChattingParticipantEntity;
    @Autowired
    private final ToChattingParticipantResponse toChattingParticipantResponse;

    @Transactional
    public ResultResponse createChattingRoom(ChattingRoomCreateRequest chattingRoomCreateRequest) {
        ChattingRoom chattingRoom = toChattingRoomEntity.toChattingRoomCreateEntity(chattingRoomCreateRequest);
        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);

        List<ChattingParticipantCreateResponse> chattingParticipantCreateResponseList = new ArrayList<>();

        for(ChattingParticipantCreateRequest chattingParticipantCreateRequest: chattingRoomCreateRequest.getChattingParticipantCreateRequestList()) {
            ChattingParticipant chattingParticipant = toChattingParticipantEntity.toChattingParticipantCreateEntity(savedChattingRoom, chattingParticipantCreateRequest);
            ChattingParticipant savedChattingParticipant = chattingParticipantRepository.save(chattingParticipant);
            ChattingParticipantCreateResponse chattingParticipantCreateResponse = toChattingParticipantResponse.toChattingParticipantCreateResponse(savedChattingParticipant);
            chattingParticipantCreateResponseList.add(chattingParticipantCreateResponse);
        }
        ChattingRoomCreateResponse chattingRoomCreateResponse = toChattingRoomResponse.toChattingRoomCreateResponse(savedChattingRoom, chattingParticipantCreateResponseList);

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_CREATE_SUCCESS, chattingRoomCreateResponse);

        return resultResponse;
    }

}
