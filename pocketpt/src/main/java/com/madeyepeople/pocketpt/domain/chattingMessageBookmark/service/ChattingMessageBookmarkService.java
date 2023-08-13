package com.madeyepeople.pocketpt.domain.chattingMessageBookmark.service;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageGetResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingMessage.mapper.ToChattingMessageResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.repository.ChattingMessageRepository;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.dto.response.ChattingMessageBookmarkCreateResponse;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.dto.response.ChattingMessageBookmarkGetListPaginationResponse;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.entity.ChattingMessageBookmark;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.mapper.ToChattingMessageBookmarkEntity;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.mapper.ToChattingMessageBookmarkResponse;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.repository.ChattingMessageBookmarkRepository;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingParticipant.repository.ChattingParticipantRepository;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.domain.chattingRoom.repository.ChattingRoomRepository;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingMessageBookmarkService {
    private final AccountRepository accountRepository;
    private final ChattingMessageRepository chattingMessageRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingParticipantRepository chattingParticipantRepository;
    private final ChattingMessageBookmarkRepository chattingMessageBookmarkRepository;
    private final ToChattingMessageBookmarkEntity toChattingMessageBookmarkEntity;
    private final ToChattingMessageBookmarkResponse toChattingMessageBookmarkResponse;
    private final ToChattingMessageResponse toChattingMessageResponse;

    @Transactional
    public ResultResponse createChattingMessageBookmark(Long roomId, Long accountId, Long chattingMessageId) {
        log.info("=======================");
        log.info("CHATTING-MESSAGE-BOOKMARK-SERVICE: [createChattingMessageBookmark] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(roomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] 북마크 account id 유효성 검사
        Account account = accountRepository.findByAccountIdAndIsDeletedFalse(accountId).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [3] 메시지 유효성 검사
        ChattingMessage chattingMessage = chattingMessageRepository.findById(chattingMessageId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_MESSAGE_NOT_FOUND, CustomExceptionMessage.CHATTING_MESSAGE_NOT_FOUND.getMessage())
        );

        // [3] 북마크 저장
        ChattingMessageBookmark chattingMessageBookmark = toChattingMessageBookmarkEntity.toChattingMessageBookmarkEntity(chattingMessage, foundChattingRoom, account);
        ChattingMessageBookmark savedChattingMessageBookmark = chattingMessageBookmarkRepository.save(chattingMessageBookmark);
        ChattingMessageBookmarkCreateResponse chattingMessageBookmarkCreateResponse = toChattingMessageBookmarkResponse.toChattingMessageBookmarkCreateResponse(savedChattingMessageBookmark);

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_BOOKMARK_CREATE_SUCCESS, chattingMessageBookmarkCreateResponse);

        log.info("CHATTING-MESSAGE-BOOKMARK-SERVICE: [createChattingMessageBookmark] END");
        log.info("=======================");

        return resultResponse;
    }

    @Transactional
    public ResultResponse getChattingMessageBookmarkListByRoomAndAccount(Long roomId, Long accountId, Pageable pageable) {
        log.info("=======================");
        log.info("CHATTING-MESSAGE-BOOKMARK-SERVICE: [getChattingMessageBookmarkListByRoomAndAccount] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(roomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] 북마크 account id 유효성 검사
        Account account = accountRepository.findByAccountIdAndIsDeletedFalse(accountId).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [3] 북마크 리스트 가져오기
        Slice<ChattingMessageBookmark> chattingMessageBookmarkList = chattingMessageBookmarkRepository.findAllByAccountAndChattingRoom(account, foundChattingRoom, pageable);
        List<ChattingMessageGetResponse> chattingMessageBookmarkCreateResponseList = new ArrayList<>();
        chattingMessageBookmarkList.forEach(c -> chattingMessageBookmarkCreateResponseList.add(toChattingMessageResponse.toChattingMessageGetResponse(c.getChattingMessage())));

        ChattingMessageBookmarkGetListPaginationResponse chattingMessageBookmarkGetListPaginationResponse = toChattingMessageBookmarkResponse.toChattingMessageBookmarkGetListPaginationResponse(chattingMessageBookmarkCreateResponseList, chattingMessageBookmarkList);
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_BOOKMARK_LIST_GET_SUCCESS, chattingMessageBookmarkGetListPaginationResponse);

        log.info("CHATTING-MESSAGE-BOOKMARK-SERVICE: [getChattingMessageBookmarkListByRoomAndAccount] END");
        log.info("=======================");

        return resultResponse;
    }

    public ResultResponse removeChattingMessageBookmark(Long roomId, Long accountId, Long chattingMessageId) {
        log.info("=======================");
        log.info("CHATTING-MESSAGE-BOOKMARK-SERVICE: [removeChattingMessageBookmark] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(roomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] 북마크 account id 유효성 검사
        Account account = accountRepository.findByAccountIdAndIsDeletedFalse(accountId).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [3] 채팅 메시지 유효성 검사
        ChattingMessage chattingMessage = chattingMessageRepository.findById(chattingMessageId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_MESSAGE_NOT_FOUND, CustomExceptionMessage.CHATTING_MESSAGE_NOT_FOUND.getMessage())
        );

        // [4] 북마크 삭제
        ChattingMessageBookmark chattingMessageBookmark = chattingMessageBookmarkRepository.findByChattingMessageAndChattingRoomAndAccount(chattingMessage, foundChattingRoom, account).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_MESSAGE_BOOKMARK_NOT_FOUND, CustomExceptionMessage.CHATTING_MESSAGE_BOOKMARK_NOT_FOUND.getMessage())
        );
        chattingMessageBookmarkRepository.delete(chattingMessageBookmark);

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_BOOKMARK_DELETE_SUCCESS, "delete success");

        log.info("CHATTING-MESSAGE-BOOKMARK-SERVICE: [removeChattingMessageBookmark] END");
        log.info("=======================");

        return resultResponse;
    }
}
