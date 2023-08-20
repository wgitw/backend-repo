package com.madeyepeople.pocketpt.domain.chattingRoom.service;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageGetResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageGetResponseForCreateRoom;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingMessage.mapper.ToChattingMessageResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.repository.ChattingMessageRepository;
import com.madeyepeople.pocketpt.domain.chattingParticipant.dto.response.ChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingParticipant.mapper.ToChattingParticipantEntity;
import com.madeyepeople.pocketpt.domain.chattingParticipant.mapper.ToChattingParticipantResponse;
import com.madeyepeople.pocketpt.domain.chattingParticipant.repository.ChattingParticipantRepository;
import com.madeyepeople.pocketpt.domain.chattingRoom.comparator.ChattingRoomListComparator;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.request.ChattingRoomCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomGetResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.domain.chattingRoom.mapper.ToChattingRoomEntity;
import com.madeyepeople.pocketpt.domain.chattingRoom.mapper.ToChattingRoomResponse;
import com.madeyepeople.pocketpt.domain.chattingRoom.repository.ChattingRoomRepository;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
        log.info("=======================");
        log.info("CHATTING-ROOM-SERVICE: [createChattingRoom] START");

        // [1] ChattingRoom 내용 저장 - 이미 hostParticipantId는 검증된 상태에서 오기 때문에 이 단계에서 검증은 불필요
        ChattingRoom chattingRoom = toChattingRoomEntity.toChattingRoomCreateEntity(hostAccountId);
        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);

        // [2] ChattingParticipant List 저장 맟 정보 담기
        List<ChattingParticipantResponse> chattingParticipantResponseList = new ArrayList<>();

        // [2-1] Chatting Host 저장 및 정보 담기
        // host의 account 정보 GET
        Account hostAccount = accountRepository.findByAccountIdAndIsDeletedFalse(hostAccountId).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        // participant에 SAVE
        ChattingParticipant chattingHost = toChattingParticipantEntity.toChattingHostCreateEntity(savedChattingRoom, hostAccount);
        ChattingParticipant savedChattingHost = chattingParticipantRepository.save(chattingHost);
        // response로 변환
        ChattingParticipantResponse chattingHostResponse = toChattingParticipantResponse.toChattingRoomCreateResponse(savedChattingHost);
        // list에 추가
        chattingParticipantResponseList.add(chattingHostResponse);

        // [2-2] Chatting Participant 저장 및 정보 담기
        // participant의 account 정보 GET
        Account participantAccount = accountRepository.findByAccountIdAndIsDeletedFalse(chattingRoomCreateRequest.getAccountId()).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
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

        log.info("CHATTING-ROOM-SERVICE: [createChattingRoom] END");
        log.info("=======================");

        return resultResponse;
    }

    @Transactional
    public ResultResponse createChattingRoomFromPtMatching(Account trainerAccount, Account traineeAccount) {
        log.info("=======================");
        log.info("CHATTING-ROOM-SERVICE: [createChattingRoomFromPtMatching] START");

        // [1] ChattingRoom 내용 저장 - 이미 hostParticipantId는 검증된 상태에서 오기 때문에 이 단계에서 검증은 불필요
        ChattingRoom chattingRoom = toChattingRoomEntity.toChattingRoomCreateEntity(traineeAccount.getAccountId());
        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);

        // [2] ChattingParticipant List 저장 맟 정보 담기
        List<ChattingParticipantResponse> chattingParticipantResponseList = new ArrayList<>();

        // [2-1] Chatting Host 저장 및 정보 담기
        // participant에 SAVE
        ChattingParticipant chattingHost = toChattingParticipantEntity.toChattingHostCreateEntity(savedChattingRoom, trainerAccount);
        ChattingParticipant savedChattingHost = chattingParticipantRepository.save(chattingHost);
        // response로 변환
        ChattingParticipantResponse chattingHostResponse = toChattingParticipantResponse.toChattingRoomCreateResponse(savedChattingHost);
        // list에 추가
        chattingParticipantResponseList.add(chattingHostResponse);

        // [2-2] Chatting Participant 저장 및 정보 담기
        // participant에 SAVE
        ChattingParticipant chattingParticipant = toChattingParticipantEntity.toChattingParticipantCreateEntity(savedChattingRoom, traineeAccount);
        ChattingParticipant savedChattingParticipant = chattingParticipantRepository.save(chattingParticipant);
        // response로 변환
        ChattingParticipantResponse chattingParticipantResponse = toChattingParticipantResponse.toChattingRoomCreateResponse(savedChattingParticipant);
        // list에 추가
        chattingParticipantResponseList.add(chattingParticipantResponse);

        // [3] Response 만들기
        ChattingRoomResponse chattingRoomResponse = toChattingRoomResponse.toChattingRoomCreateResponse(savedChattingRoom, chattingParticipantResponseList, traineeAccount.getNickname());

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_CREATE_SUCCESS, chattingRoomResponse);

        log.info("CHATTING-ROOM-SERVICE: [createChattingRoomFromPtMatching] END");
        log.info("=======================");

        return resultResponse;
    }

    // 채팅방 입장
    @Transactional
    public void chattingRoomEnter(String accountUsername, Long chattingRoomId, String simpSessionId) {
        log.info("=======================");
        log.info("CHATTING-ROOM-SERVICE: [chattingRoomEnter] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom chattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] 채팅 sender 유효성 검사
        Account account = accountRepository.findByEmailAndIsDeletedFalse(accountUsername).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        ChattingParticipant chattingParticipant = chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, chattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [3] 채팅방 입장 시간 및 simpSessionId update
        chattingParticipant.setChattingRoomEntryTime(LocalDateTime.now());
        chattingParticipant.setSimpSessionId(simpSessionId);
        chattingParticipantRepository.save(chattingParticipant);

        // [4] 채팅방에서 그동안 읽지 않았던 메시지 읽음 처리
        // [4-1] 채팅방에 해당하면서 다른 유저가 보낸 읽지 않은 메시지 개수: (원래 읽지 않은 메시지 개수) -1 처리
        chattingMessageRepository.updateAllByNotViewCountMinusOneByRoomIdAndChattingAccountId(chattingRoomId, chattingParticipant.getAccount().getAccountId());

        // [4-2] 채팅방에 해당하면서 내가 읽지 않은 메시지 개수: 0 처리
        chattingParticipantRepository.updateAllByNotViewCountZeroByRoomIdAndAccountIdAndIsDeletedFalse(chattingRoomId, chattingParticipant.getAccount().getAccountId());

        log.info("CHATTING-ROOM-SERVICE: [chattingRoomEnter] END");
        log.info("=======================");
    }

    // 채팅방 퇴장
    @Transactional
    public void chattingRoomExit(String simpSessionId) {
        log.info("=======================");
        log.info("CHATTING-ROOM-SERVICE: [chattingRoomExit] START");

        // [1] 채팅방 유효성 검사
        Optional<ChattingParticipant> foundChattingParticipant = chattingParticipantRepository.findBySimpSessionIdAndIsDeletedFalse(simpSessionId);
        // [2] 채팅방 퇴장 시간 및 simpSessionId update
        if (foundChattingParticipant.isPresent()) {
            ChattingParticipant chattingParticipant = foundChattingParticipant.get();
            chattingParticipant.setChattingRoomExitTime(LocalDateTime.now());
            chattingParticipant.setSimpSessionId(null);
            chattingParticipantRepository.save(chattingParticipant);
        }

        log.info("CHATTING-ROOM-SERVICE: [chattingRoomExit] END");
        log.info("=======================");
    }

    // 채팅방 리스트 - 메시지순으로 전체 조회
    @Transactional
    public ResultResponse getChattingRoomListByUser(Long accountId) {
        log.info("=======================");
        log.info("CHATTING-ROOM-SERVICE: [getChattingRoomListByUser] START");

        // [1] accountId 유효성 체크
        Account account = accountRepository.findByAccountIdAndIsDeletedFalse(accountId).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );

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

        // [5] 최신 메시지 created_at desc로 정렬, 최신 메시지가 없다면 방이 만들어진 시간으로 대체
        Collections.sort(chattingRoomResponseList, new ChattingRoomListComparator().reversed());

        // [6] resultResponse 만들기
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_LIST_GET_SUCCESS, chattingRoomResponseList);

        log.info("CHATTING-ROOM-SERVICE: [getChattingRoomListByUser] END");
        log.info("=======================");

        return resultResponse;
    }

    @Transactional
    public ResultResponse updateChattingRoomInfoForNewMessage(Account account, Long chattingRoomId, Long latestChattingMessageId) {
        log.info("=======================");
        log.info("CHATTING-ROOM-SERVICE: [updateChattingRoomInfoForNewMessage] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] account 유효성 검사
        ChattingParticipant foundChattingParticipant = chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [3] 최근 ChattingMessage 찾기
        ChattingMessage chattingMessage = chattingMessageRepository.findById(latestChattingMessageId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_MESSAGE_NOT_FOUND, CustomExceptionMessage.CHATTING_MESSAGE_NOT_FOUND.getMessage())
        );
        ChattingMessageGetResponse chattingMessageGetResponse = toChattingMessageResponse.toChattingMessageGetResponseForUpdateChattingRoomList(chattingMessage, foundChattingParticipant);

        // [5] resultResponse 만들기
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_LIST_UPDATE_INFO_FOR_MESSAGE_GET_SUCCESS, chattingMessageGetResponse);

        log.info("CHATTING-ROOM-SERVICE: [updateChattingRoomInfoForNewMessage] END");
        log.info("=======================");

        return resultResponse;
    }

    @Transactional
    public ResultResponse updateChattingRoomInfoForNewRoom(Account account, Long chattingRoomId, Long hostAccountId) {
        log.info("=======================");
        log.info("CHATTING-ROOM-SERVICE: [updateChattingRoomInfoForNewRoom] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] account 유효성 검사
        ChattingParticipant foundChattingParticipant = chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [3] hostAccount 유효성 검사
        Account hostAccount = accountRepository.findByAccountIdAndIsDeletedFalse(hostAccountId).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        ChattingParticipant foundHostChattingParticipant = chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(hostAccount, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [4] resultResponse 만들기
        ChattingMessageGetResponseForCreateRoom chattingMessageGetResponseForCreateRoom = toChattingMessageResponse.toChattingMessageGetResponseForRoom(foundChattingRoom, foundHostChattingParticipant);
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_LIST_UPDATE_INFO_FOR_ROOM_GET_SUCCESS, chattingMessageGetResponseForCreateRoom);

        log.info("CHATTING-ROOM-SERVICE: [updateChattingRoomInfoForNewRoom] END");
        log.info("=======================");

        return resultResponse;
    }

    // chattingRoom 삭제
    @Transactional
    public ResultResponse deleteChattingRoom(Account account, Long chattingRoomId) {
        log.info("=======================");
        log.info("CHATTING-ROOM-SERVICE: [deleteChattingRoom] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] account 유효성 검사
        chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [3] 채팅방에 속한 참여자 리스트 가져와서 isDeleted를 true로 변경
        // [3-1] 채팅방에 속한 참여자 리스트 가져오기
        List<ChattingParticipant> chattingParticipantList = chattingParticipantRepository.findByChattingRoomAndIsDeletedFalse(foundChattingRoom);
        // [3-2] 채팅방에 속한 참여자들의 isDeleted를 true로 변경
        for(ChattingParticipant c : chattingParticipantList) {
            c.setIsDeleted(true);
            chattingParticipantRepository.save(c);
        }

        // [4] 채팅방 삭제
        foundChattingRoom.setIsDeleted(true);
        chattingRoomRepository.save(foundChattingRoom);

        // [5] resultResponse 만들기
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_ROOM_DELETE_SUCCESS, "채팅방 삭제 성공");

        log.info("CHATTING-ROOM-SERVICE: [deleteChattingRoom] END");
        log.info("=======================");

        return resultResponse;
    }
}
