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

        // [2] ChattingParticipant List 저장 맟 정보 담기
        List<ChattingParticipantResponse> chattingParticipantResponseList = new ArrayList<>();

        // [2-1] Chatting Host 저장 및 정보 담기
        // host의 account 정보 GET
        Account hostAccount = accountRepository.findByAccountIdAndIsDeletedFalse(hostAccountId).orElseThrow();
        // participant에 SAVE
        ChattingParticipant chattingHost = toChattingParticipantEntity.toChattingHostCreateEntity(savedChattingRoom, hostAccount);
        ChattingParticipant savedChattingHost = chattingParticipantRepository.save(chattingHost);
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

    // 채팅방 리스트 - 메시지순으로 전체 조회
    @Transactional
    public ResultResponse getChattingRoomListByUser(Long accountId, Pageable pageable) {
        // TODO: 최신 메시지 created_at desc로 정렬할 것, 최신 메시지가 없다면 방이 만들어진 시간으로 대체

        // [1] accountId 유효성 체크
        Account account = accountRepository.findByAccountIdAndIsDeletedFalse(accountId).orElseThrow();

        // [2] account에서 participant 리스트 정보 받아오기
        List<ChattingParticipant> chattingParticipantList = account.getChattingParticipantList();

        // [3] select된 ChattingParticipant에서 room list 정보 리스트에 담기
        List<ChattingRoomGetResponse> chattingRoomResponseList = new ArrayList<>();
        for(ChattingParticipant chattingParticipant:chattingParticipantList) {
            ChattingRoom chattingRoom = chattingParticipant.getChattingRoom();
            String roomName = "";
            int notViewCount = 0;

            // [3-1] room 정보에 포함되어 있는 participant list 정보 담기 - 본인 제외
            List<ChattingParticipantResponse> chattingParticipantResponseList = new ArrayList<>();
            for(ChattingParticipant c: chattingRoom.getChattingParticipantList()) {
                if(accountId.equals(c.getAccount().getAccountId())) {
                    notViewCount = c.getNotViewCount();
                } else {
                    ChattingParticipantResponse chattingParticipantResponse = toChattingParticipantResponse.toChattingParticipantCreateResponse(c);
                    roomName += c.getAccount().getNickname();
                    chattingParticipantResponseList.add(chattingParticipantResponse);
                }
            }

            // [3-2] 최신 메시지 정보 가져오기
            Optional<ChattingMessage> chattingMessage = chattingMessageRepository.findLatestChattingMessageByRoom(chattingRoom.getChattingRoomId());
            ChattingRoomGetResponse chattingRoomGetResponse;
            if(chattingMessage.isPresent()) { // 채팅 내용이 존재하는 경우
                chattingRoomGetResponse = toChattingRoomResponse.toChattingRoomListGetResponse(chattingRoom, roomName, notViewCount, chattingParticipantResponseList, chattingMessage.get());
            }
            else { // 채팅방이 새롭게 개설되어 채팅 내용이 존재하지 않는 경우
                ChattingMessage defaultChattingMessage = new ChattingMessage();
                chattingRoomGetResponse = toChattingRoomResponse.toChattingRoomListGetResponse(chattingRoom, roomName, notViewCount, chattingParticipantResponseList, defaultChattingMessage);
            }
            chattingRoomResponseList.add(chattingRoomGetResponse);
        }

        // [5] resultResponse 만들기
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_LIST_GET_SUCCESS, chattingRoomResponseList);

        return resultResponse;
    }
}
