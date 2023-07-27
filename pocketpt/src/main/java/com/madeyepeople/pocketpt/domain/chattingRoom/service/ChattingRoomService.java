package com.madeyepeople.pocketpt.domain.chattingRoom.service;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageGetResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingMessage.mapper.ToChattingMessageResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.repository.ChattingMessageRepository;
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

import java.time.LocalDateTime;
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

    private final AccountRepository accountRepository;

    // 채팅방 생성
    @Transactional
    public ResultResponse createChattingRoom(Long hostAccountId,
                                             ChattingRoomCreateRequest chattingRoomCreateRequest) {
        // [1] ChattingRoom 내용 저장 - 이미 hostParticipantId는 검증된 상태에서 오기 때문에 이 단계에서 검증은 불필요
        ChattingRoom chattingRoom = toChattingRoomEntity.toChattingRoomCreateEntity(hostAccountId);
        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);
        // headerAccessor.getSessionAttributes().put("chattingRoomId", savedChattingRoom.getChattingRoomId());

        // [2] ChattingParticipant List 저장 맟 정보 담기
        List<ChattingParticipantResponse> chattingParticipantResponseList = new ArrayList<>();

        // [2-1] Chatting Host 저장 및 정보 담기
        // host의 account 정보 GET
        Account hostAccount = accountRepository.findByAccountIdAndIsDeletedFalse(hostAccountId).orElseThrow();
        // participant에 SAVE
        ChattingParticipant chattingHost = toChattingParticipantEntity.toChattingHostCreateEntity(savedChattingRoom, hostAccount);
        ChattingParticipant savedChattingHost = chattingParticipantRepository.save(chattingHost);
        // Websocket Session에 참여자 정보 SAVE
        // headerAccessor.getSessionAttributes().put("chattingParticipantId", savedChattingHost.getChattingParticipantId());
        // response로 변환
        ChattingParticipantResponse chattingHostResponse = toChattingParticipantResponse.toChattingRoomCreateResponse(savedChattingHost);
        // list에 추가
        chattingParticipantResponseList.add(chattingHostResponse);

        // [2-2] Chatting Participant 저장 및 정보 담기
        // participant의 account 정보 GET
        Account participantAccount = accountRepository.findByAccountIdAndIsDeletedFalse(chattingRoomCreateRequest.getAccountId()).orElseThrow();
        // participant에 SAVE
        ChattingParticipant chattingParticipant = toChattingParticipantEntity.toChattingParticipantCreateEntity(savedChattingRoom, participantAccount);
        ChattingParticipant savedChattingParticipant = chattingParticipantRepository.save(chattingParticipant);
        // Websocket Session에 참여자 정보 SAVE
        // headerAccessor.getSessionAttributes().put("chattingParticipantId", savedChattingParticipant.getChattingParticipantId());
        // response로 변환
        ChattingParticipantResponse chattingParticipantResponse = toChattingParticipantResponse.toChattingRoomCreateResponse(savedChattingParticipant);
        // list에 추가
        chattingParticipantResponseList.add(chattingParticipantResponse);

        // [3] Response 만들기
        ChattingRoomResponse chattingRoomResponse = toChattingRoomResponse.toChattingRoomCreateResponse(savedChattingRoom, chattingParticipantResponseList, participantAccount.getNickname());

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_CREATE_SUCCESS, chattingRoomResponse);

        return resultResponse;
    }

    // 채팅방 입장
    @Transactional
    public void chattingRoomEnter(String accountUsername, Long chattingRoomId, String simpSessionId) {
        // [1] 채팅방 유효성 검사
        ChattingRoom chattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow();

        // [2] 채팅 sender 유효성 검사
        Account account = accountRepository.findByEmailAndIsDeletedFalse(accountUsername).orElseThrow();
        ChattingParticipant chattingParticipant = chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, chattingRoom).orElseThrow();

        // [3] 채팅방 입장 시간 및 simpSessionId update
        chattingParticipant.setChattingRoomEntryTime(LocalDateTime.now());
        chattingParticipant.setSimpSessionId(simpSessionId);
        chattingParticipantRepository.save(chattingParticipant);

        // [4] 채팅방에서 그동안 읽지 않았던 메시지 읽음 처리
        // [4-1] 채팅방에 해당하면서 다른 유저가 보낸 읽지 않은 메시지 개수: (원래 읽지 않은 메시지 개수) -1 처리
        chattingMessageRepository.updateAllByNotViewCountMinusOneByRoomIdAndChattingAccountId(chattingRoomId, chattingParticipant.getAccount().getAccountId());

        // [4-2] 채팅방에 해당하면서 내가 읽지 않은 메시지 개수: 0 처리
        chattingParticipantRepository.updateAllByNotViewCountZeroByRoomIdAndAccountIdAndIsDeletedFalse(chattingRoomId, chattingParticipant.getAccount().getAccountId());

        // socket session에 저장
//        simpMessageHeaderAccessor.getSessionAttributes().put("participantId", chattingParticipant.getChattingParticipantId());
//        simpMessageHeaderAccessor.getSessionAttributes().put("roomId", chattingRoomEnterRequest.getChattingRoomId());
    }

    // 채팅방 퇴장
    @Transactional
    public void chattingRoomExit(String simpSessionId) {
        Optional<ChattingParticipant> foundChattingParticipant = chattingParticipantRepository.findBySimpSessionIdAndIsDeletedFalse(simpSessionId);
        // [1] 채팅방 퇴장 시간 및 simpSessionId update
        if (foundChattingParticipant.isPresent()) {
            ChattingParticipant chattingParticipant = foundChattingParticipant.get();
            chattingParticipant.setChattingRoomExitTime(LocalDateTime.now());
            chattingParticipant.setSimpSessionId(null);
            chattingParticipantRepository.save(chattingParticipant);
        }
    }


//    @Transactional
//    public ResultResponse createChattingRoom(Long hostParticipant,
//                                             ChattingRoomCreateByParticipantListRequest chattingRoomCreateRequest,
//                                             SimpMessageHeaderAccessor headerAccessor) {
//        // 1:1로만 개발하는걸로 수정 -> 나중에 다:대로 열기
//            // 일단 해! 그러고 갈아엎든 그때 해!
//        // hostParticipant id -> 이미 검증된 id 이므로 이건 그냥 바로 넣고
//        // 방이름 삭제 -> 참여자 이름이 방이름일거라서 방이름 삭제하기
//
//        // 방 입장 시간 체크
//        // 방 생성: host만 입장한걸로 생각
//            // 방 생성도 socket api로 만들어야 하나?
//            // 입장했으니까 바로 socket 열고 입장시켜줘야 함
//        // 방 입장: 이후에는 개별적으로 입장 퇴장 생각
//
//        // [1] ChattingRoom 내용 저장
//        ChattingRoom chattingRoom = toChattingRoomEntity.toChattingRoomCreateEntity(chattingRoomCreateRequest);
//        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);
//        headerAccessor.getSessionAttributes().put("chattingRoomId", savedChattingRoom.getChattingRoomId());
//
//        // [2] ChattingParticipant list 저장 맟 정보 담기
//        List<ChattingParticipantResponse> chattingParticipantResponseList = new ArrayList<>();
//        for(ChattingParticipantCreateRequest chattingParticipantCreateRequest: chattingRoomCreateRequest.getChattingParticipantCreateRequestList()) {
//            ChattingParticipant chattingParticipant = toChattingParticipantEntity.toChattingParticipantCreateEntity(savedChattingRoom, chattingParticipantCreateRequest);
//            ChattingParticipant savedChattingParticipant = chattingParticipantRepository.save(chattingParticipant);
//
//            headerAccessor.getSessionAttributes().put("chattingParticipantId", savedChattingParticipant.getChattingParticipantId());
//
//            ChattingParticipantResponse chattingParticipantResponse = toChattingParticipantResponse.toChattingParticipantCreateResponse(savedChattingParticipant);
//            chattingParticipantResponseList.add(chattingParticipantResponse);
//        }
//
//        // [3] Response 만들기
//        ChattingRoomResponse chattingRoomResponse = toChattingRoomResponse.toChattingRoomCreateResponse(savedChattingRoom, chattingParticipantResponseList);
//
//        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_CREATE_SUCCESS, chattingRoomResponse);
//
//        return resultResponse;
//    }

    @Transactional
    public ResultResponse getChattingRoomListByUser(Long accountId, Pageable pageable) {
        // [1] accountId 유효성 체크
        Account account = accountRepository.findByAccountIdAndIsDeletedFalse(accountId).orElseThrow();

        // [2] ChattingParticipant에서 participantId로 select
        List<ChattingParticipant> chattingParticipantList = chattingParticipantRepository.findAllByAccountAndIsDeletedFalse(account);

        // [3] select된 ChattingParticipant에서 room list 정보 리스트에 담기
        List<ChattingRoomGetResponse> chattingRoomResponseList = new ArrayList<>();
        for(ChattingParticipant chattingParticipant: chattingParticipantList) {
            ChattingRoom chattingRoom = chattingParticipant.getChattingRoom();

            // [3-1] room 정보에 포함되어 있는 participant list 정보 담기 - 본인 제외
            // TODO: account 테이블 조회 후 프로필 사진과 이름 받아와서 그 정보로 변경할 것
            List<ChattingParticipantResponse> chattingParticipantResponseList = new ArrayList<>();
            for(ChattingParticipant chattingRoomParticipant: chattingRoom.getChattingParticipantList()) {
                if(!accountId.equals(chattingRoomParticipant.getAccount().getAccountId())) {
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
//        ChattingRoomListPaginationResponse chattingRoomListPaginationResponse = ToChattingRoomResponse.toChattingRoomListPaginationResponse(chattingParticipantList, chattingRoomResponseList);
//
//        // [5] resultResponse 만들기
//        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_LIST_GET_SUCCESS, chattingRoomListPaginationResponse);
//
//        return resultResponse;
        return null;
    }

    // 채팅 메시지 리스트 최신 100개
    @Transactional
    public ResultResponse getChattingMessageListByRoom(Long chattingRoomId) {
        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow();

        // [2] ChattingMessage list 가져와서 정보 담기
        List<ChattingMessage> chattingMessageList = chattingMessageRepository.findAllByChattingRoom(foundChattingRoom.getChattingRoomId());
        List<ChattingMessageGetResponse> chattingMessageGetResponseList = new ArrayList<>();
        chattingMessageList.forEach(c -> chattingMessageGetResponseList.add(toChattingMessageResponse.toChattingMessageGetResponse(c)));
        System.out.println("===============");
        chattingMessageGetResponseList.forEach(c -> System.out.println(c.getContent()));
        System.out.println("===============");

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_LIST_GET_SUCCESS, chattingMessageGetResponseList);

        return resultResponse;
    }

    @Transactional
    public ResultResponse getChattingFileListByRoom(Long chattingRoomId) {
        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow();

        // [2] ChattingMessage list 가져와서 정보 담기
        List<ChattingMessage> chattingMessageList = chattingMessageRepository.findAllFileUrlByChattingRoom(foundChattingRoom.getChattingRoomId());
        List<ChattingMessageGetResponse> chattingMessageGetResponseList = new ArrayList<>();
        chattingMessageList.forEach(c -> chattingMessageGetResponseList.add(toChattingMessageResponse.toChattingMessageGetResponse(c)));

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_LIST_GET_SUCCESS, chattingMessageGetResponseList);

        return resultResponse;
    }

}
