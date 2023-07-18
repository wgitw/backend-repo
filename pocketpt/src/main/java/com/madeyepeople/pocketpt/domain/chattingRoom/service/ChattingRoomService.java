package com.madeyepeople.pocketpt.domain.chattingRoom.service;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageGetResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingMessage.mapper.ToChattingMessageResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.repository.ChattingMessageRepository;
import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.request.ChattingParticipantCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingParticipant.mapper.ToChattingParticipantEntity;
import com.madeyepeople.pocketpt.domain.chattingParticipant.mapper.ToChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.repository.ChattingParticipantRepository;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.request.ChattingRoomCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomGetResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomListPaginationResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.domain.chattingRoom.mapper.ToChattingRoomEntity;
import com.madeyepeople.pocketpt.domain.chattingRoom.mapper.ToChattingRoomResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.repository.ChattingRoomRepository;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingRoomService {
    private final ChattingRoomRepository chattingRoomRepository;

    private final ChattingParticipantRepository chattingParticipantRepository;

    private final ChattingMessageRepository chattingMessageRepository;

    private final ToChattingRoomEntity toChattingRoomEntity;

    private final ToChattingRoomResponse toChattingRoomResponse;

    private final ToChattingParticipantEntity toChattingParticipantEntity;

    private final ToChattingParticipantResponse toChattingParticipantResponse;

    private final ToChattingMessageResponse toChattingMessageResponse;

    @Transactional
    public ResultResponse createChattingRoom(ChattingRoomCreateRequest chattingRoomCreateRequest) {
        // TODO: account 테이블 조회 후 프로필 사진과 이름 받아와서 chattingRoomName 설정할 것

        // [1] ChattingRoom 내용 저장
        ChattingRoom chattingRoom = toChattingRoomEntity.toChattingRoomCreateEntity(chattingRoomCreateRequest);
        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);

        // [2] ChattingParticipant list 저장 맟 정보 담기
        List<ChattingParticipantResponse> chattingParticipantResponseList = new ArrayList<>();
        for(ChattingParticipantCreateRequest chattingParticipantCreateRequest: chattingRoomCreateRequest.getChattingParticipantCreateRequestList()) {
            ChattingParticipant chattingParticipant = toChattingParticipantEntity.toChattingParticipantCreateEntity(savedChattingRoom, chattingParticipantCreateRequest);
            ChattingParticipant savedChattingParticipant = chattingParticipantRepository.save(chattingParticipant);
            ChattingParticipantResponse chattingParticipantResponse = toChattingParticipantResponse.toChattingParticipantCreateResponse(savedChattingParticipant);
            chattingParticipantResponseList.add(chattingParticipantResponse);
        }

        // [3] Response 만들기
        ChattingRoomResponse chattingRoomResponse = toChattingRoomResponse.toChattingRoomCreateResponse(savedChattingRoom, chattingParticipantResponseList);

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_CREATE_SUCCESS, chattingRoomResponse);

        return resultResponse;
    }

    @Transactional
    public ResultResponse getChattingRoomListByUser(Long accountId, Pageable pageable) {
        // TODO: [1] accountId 유효성 체크

        // [2] ChattingParticipant에서 participantId로 select
        Slice<ChattingParticipant> chattingParticipantList = chattingParticipantRepository.findAllByParticipantIdAndIsDeletedFalse(accountId, pageable);

        // [3] select된 ChattingParticipant에서 room list 정보 리스트에 담기
        List<ChattingRoomGetResponse> chattingRoomResponseList = new ArrayList<>();
        for(ChattingParticipant chattingParticipant: chattingParticipantList.getContent()) {
            ChattingRoom chattingRoom = chattingParticipant.getChattingRoom();

            // [3-1] room 정보에 포함되어 있는 participant list 정보 담기 - 본인 제외
            // TODO: account 테이블 조회 후 프로필 사진과 이름 받아와서 그 정보로 변경할 것
            List<ChattingParticipantResponse> chattingParticipantResponseList = new ArrayList<>();
            for(ChattingParticipant chattingRoomParticipant: chattingRoom.getChattingParticipantList()) {
                if(!accountId.equals(chattingRoomParticipant.getParticipantId())) {
                    ChattingParticipantResponse chattingParticipantResponse = toChattingParticipantResponse.toChattingParticipantCreateResponse(chattingRoomParticipant);
                    chattingParticipantResponseList.add(chattingParticipantResponse);
                }
            }

            // [3-2] 최신 메시지 정보 가져오기
            // TODO: 최신 메시지 읽음 유무 확인할 것
            // TODO: 최신 메시지 created_at desc로 정렬할 것
            Optional<ChattingMessage> chattingMessage = chattingMessageRepository.findLatestChattingMessageByRoom(chattingRoom.getChattingRoomId());
            ChattingRoomGetResponse chattingRoomGetResponse;
            if(chattingMessage.isPresent()) { // 채팅 내용이 존재하는 경우
                chattingRoomGetResponse = toChattingRoomResponse.toChattingRoomListGetResponse(chattingRoom, chattingParticipantResponseList, chattingMessage.get());
            }
            else { // 채팅방이 새롭게 개설되어 채팅 내용이 존재하지 않는 경우
                ChattingMessage defaultChattingMessage = new ChattingMessage();
                chattingRoomGetResponse = toChattingRoomResponse.toChattingRoomListGetResponse(chattingRoom, chattingParticipantResponseList, defaultChattingMessage);
            }
            chattingRoomResponseList.add(chattingRoomGetResponse);
        }

        // [4] 페이지네이션 정보와 chattingRoom list를 response로 변환
        ChattingRoomListPaginationResponse chattingRoomListPaginationResponse = ToChattingRoomResponse.toChattingRoomListPaginationResponse(chattingParticipantList, chattingRoomResponseList);

        // [5] resultResponse 만들기
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_LIST_GET_SUCCESS, chattingRoomListPaginationResponse);

        return resultResponse;
    }

    @Transactional
    public ResultResponse getChattingMessageListByRoom(Long chattingRoomId) {
        // [1] 채팅방 유효성 검사
        Optional<ChattingRoom> foundChattingRoom = chattingRoomRepository.findByChattingRoomId(chattingRoomId);

        // [2] 채팅방 참가자 정보 가져와서 정보 담기 - 참가자 이름, 프로필 사진
        //  TODO: 참가자 정보 account에서 가져오기

        // [3] ChattingMessage list 가져와서 정보 담기
        List<ChattingMessage> chattingMessageList = chattingMessageRepository.findAllByChattingRoom(chattingRoomId);
        List<ChattingMessageGetResponse> chattingMessageGetResponseList = new ArrayList<>();
        chattingMessageList.forEach(c -> chattingMessageGetResponseList.add(toChattingMessageResponse.toChattingMessageGetResponse(c)));

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_LIST_GET_SUCCESS, chattingMessageGetResponseList);

        return resultResponse;
    }

    @Transactional
    public ResultResponse getChattingFileListByRoom(Long chattingRoomId) {
        // [1] 채팅방 유효성 검사
        Optional<ChattingRoom> foundChattingRoom = chattingRoomRepository.findByChattingRoomId(chattingRoomId);

        // [2] 채팅방 참가자 정보 가져와서 정보 담기 - 참가자 이름, 프로필 사진
        //  TODO: 참가자 정보 account에서 가져오기

        // [3] ChattingMessage list 가져와서 정보 담기
        List<ChattingMessage> chattingMessageList = chattingMessageRepository.findAllFileUrlByChattingRoom(chattingRoomId);
        List<ChattingMessageGetResponse> chattingMessageGetResponseList = new ArrayList<>();
        chattingMessageList.forEach(c -> chattingMessageGetResponseList.add(toChattingMessageResponse.toChattingMessageGetResponse(c)));

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_LIST_GET_SUCCESS, chattingMessageGetResponseList);

        return resultResponse;
    }

}
