package com.madeyepeople.pocketpt.domain.chattingMessage.service;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingFileCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingMessageContentCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.*;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingMessage.mapper.ToChattingMessageEntity;
import com.madeyepeople.pocketpt.domain.chattingMessage.mapper.ToChattingMessageResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.repository.ChattingMessageRepository;
import com.madeyepeople.pocketpt.domain.chattingMessage.repositoryInterface.ChattingMessageWithBookmarkInterface;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingParticipant.repository.ChattingParticipantRepository;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.domain.chattingRoom.repository.ChattingRoomRepository;
import com.madeyepeople.pocketpt.global.common.ScrollPagination;
import com.madeyepeople.pocketpt.global.common.ScrollPaginationFile;
import com.madeyepeople.pocketpt.global.common.ScrollPaginationMessage;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.s3.S3FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingMessageService {
    private final AccountRepository accountRepository;
    private final ChattingMessageRepository chattingMessageRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingParticipantRepository chattingParticipantRepository;
    private final ToChattingMessageEntity toChattingMessageEntity;
    private final ToChattingMessageResponse toChattingMessageResponse;
    private final S3FileService s3FileService;

    @Transactional
    public ResultResponse createChattingMessage(ChattingMessageContentCreateRequest chattingMessageContentCreateRequest, Long roomId, String accountUsername) {
        log.info("=======================");
        log.info("CHATTING-MESSAGE-SERVICE: [createChattingMessage] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(roomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );
        log.info("CHATTING-MESSAGE-SERVICE: [createChattingMessage] foundChattingRoomId>> {}", foundChattingRoom.getChattingRoomId());

        // [2] 채팅 sender 유효성 검사
        Account account = accountRepository.findByEmailAndIsDeletedFalse(accountUsername).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        ChattingParticipant foundChattingParticipant = chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );
        log.info("CHATTING-MESSAGE-SERVICE: [createChattingMessage] sender ID>> {}", foundChattingParticipant.getAccount().getAccountId());

        // [3] ChattingMessage 초기화
        ChattingMessage chattingMessage = toChattingMessageEntity.toChattingMessageCreateEntity(foundChattingParticipant, chattingMessageContentCreateRequest);
        log.info("CHATTING-MESSAGE-SERVICE: [createChattingMessage] content>> {}", chattingMessage.getContent());

        // [4] 채팅방에 참여 중인 사람이 현재 채팅방 참여 중인지 체크 - 채팅 읽음 처리 로직
        List<ChattingParticipant> chattingParticipantList = chattingParticipantRepository.findAllByChattingRoomIdAndNotInAccountIdAndIsDeletedFalse(roomId, foundChattingParticipant.getAccount().getAccountId());
        int notViewCount = foundChattingRoom.getNumberOfParticipant() - 1; // 본인을 제외한 나머지 수로 초기 세팅
        for(ChattingParticipant chattingParticipant: chattingParticipantList) {
            if (chattingParticipant.getSimpSessionId() != null) {
                // 채팅 메시지를 읽지 않은 사람의 수
                --notViewCount;
            } else if (chattingParticipant.getSimpSessionId() == null) {
                // 채팅 참여자가 읽지 않은 메시지 수
                int participantNotViewCount = chattingParticipant.getNotViewCount() + 1;
                chattingParticipant.setNotViewCount(participantNotViewCount);
                chattingParticipantRepository.save(chattingParticipant);
            }
        }
        chattingMessage.setNotViewCount(notViewCount);

        // [5] 채팅 메시지 저장 및 정보 담기
        ChattingMessage savedChattingMessage = chattingMessageRepository.save(chattingMessage);
        log.info("CHATTING-MESSAGE-SERVICE: [createChattingMessage] savedChattingMessage content>> {}", savedChattingMessage.getContent());
        ChattingMessageCreateResponse chattingMessageCreateResponse = toChattingMessageResponse.toChattingMessageCreateResponse(foundChattingRoom, account, savedChattingMessage);
        log.info("CHATTING-MESSAGE-SERVICE: [createChattingMessage] chattingMessageCreateResponse>> {}", chattingMessageCreateResponse.toString());

        // [6] 채팅방 id, 채팅 sender id, 채팅 메시지 정보가 담긴 chattingMessageCreateResponse
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_CREATE_SUCCESS, chattingMessageCreateResponse);
        log.info("CHATTING-MESSAGE-SERVICE: [createChattingMessage] resultResponse>> {}", resultResponse.toString());

        log.info("CHATTING-MESSAGE-SERVICE: [createChattingMessage] END");
        log.info("=======================");

        return resultResponse;
    }

    @Transactional
    public ResultResponse createChattingFile(ChattingFileCreateRequest chattingFileCreateRequest, Long roomId, Long accountId) {
        log.info("=======================");
        log.info("CHATTING-MESSAGE-SERVICE: [createChattingFile] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(roomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] 채팅 sender 유효성 검사
        Account account = accountRepository.findByAccountIdAndIsDeletedFalse(accountId).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        ChattingParticipant foundChattingParticipant = chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [3] 채팅 파일 S3 업로드
        String fileUrl = s3FileService.uploadFile("chatting/" + roomId + "/", chattingFileCreateRequest.getFile());

        // [4] ChattingMessage 초기화
        ChattingMessage chattingMessage = toChattingMessageEntity.toChattingFileCreateEntity(foundChattingParticipant, fileUrl);

        // [5] 채팅방에 참여 중인 사람이 현재 채팅방 참여 중인지 체크 - 채팅 읽음 처리 로직
        List<ChattingParticipant> chattingParticipantList = chattingParticipantRepository.findAllByChattingRoomIdAndNotInAccountIdAndIsDeletedFalse(roomId, accountId);
        int notViewCount = foundChattingRoom.getNumberOfParticipant() - 1; // 본인을 제외한 나머지 수로 초기 세팅
        for(ChattingParticipant chattingParticipant: chattingParticipantList) {
            if (chattingParticipant.getSimpSessionId() != null) {
                // 채팅 메시지를 읽지 않은 사람의 수
                --notViewCount;
            } else if (chattingParticipant.getSimpSessionId() == null) {
                // 채팅 참여자가 읽지 않은 메시지 수
                int participantNotViewCount = chattingParticipant.getNotViewCount();
                chattingParticipant.setNotViewCount(++participantNotViewCount);
                chattingParticipantRepository.save(chattingParticipant);
            }
        }
        chattingMessage.setNotViewCount(notViewCount);

        // [6] 채팅 메시지 저장 및 정보 담기
        ChattingMessage savedChattingMessage = chattingMessageRepository.save(chattingMessage);
        ChattingMessageCreateResponse chattingMessageCreateResponse = toChattingMessageResponse.toChattingFileCreateResponse(foundChattingRoom, account, savedChattingMessage);

        // [7] 채팅방 id, 채팅 sender id, 채팅 메시지 정보가 담긴 chattingMessageCreateResponse
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_FILE_CREATE_SUCCESS, chattingMessageCreateResponse);

        log.info("CHATTING-MESSAGE-SERVICE: [createChattingFile] END");
        log.info("=======================");

        return resultResponse;
    }

    // 채팅 메시지 리스트 최신 100개
    @Transactional
    public ResultResponse getChattingMessageListByRoom(Long chattingRoomId, int page, int size, Integer totalRecord, Long accountId) {
        log.info("=======================");
        log.info("CHATTING-MESSAGE-SERVICE: [getChattingMessageListByRoom] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] ChattingMessage list 가져와서 정보 담기
        if (totalRecord == null) {
            // 페이지네이션 전, total page 구하기
            totalRecord = chattingMessageRepository.findAllCountByChattingRoomOrderByChattingMessageId(foundChattingRoom.getChattingRoomId());
        }
        ScrollPagination scrollPagination = new ScrollPaginationMessage(size, page, totalRecord);

        log.info("CHATTING-MESSAGE-SERVICE: [getChattingMessageListByRoom] scrollPagination.getStartNum()>> {}", scrollPagination.getStartNum());

        List<ChattingMessageWithBookmarkInterface> chattingMessageList = chattingMessageRepository
                .findAllByChattingRoomOrderByChattingMessageIdDescWithBookmark(foundChattingRoom.getChattingRoomId(), scrollPagination.getStartNum(), scrollPagination.getPageSize(), accountId);
        List<ChattingMessageWithBookmarkGetResponse> chattingMessageGetResponseList = new ArrayList<>();
        chattingMessageList.forEach(c -> chattingMessageGetResponseList.add(toChattingMessageResponse.toChattingMessageWithBookmarkGetResponse(c)));

        ChattingMessageWithBookmarkGetListPaginationRespnse chattingMessageGetListPaginationRespnse =
                toChattingMessageResponse.toChattingMessageWithBookmarkGetListPaginationResponse(chattingMessageGetResponseList, scrollPagination);

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_LIST_GET_SUCCESS, chattingMessageGetListPaginationRespnse);

        log.info("CHATTING-MESSAGE-SERVICE: [getChattingMessageListByRoom] END");
        log.info("=======================");

        return resultResponse;
    }

    // 채팅방 파일 리스트 최신 100개
    @Transactional
    public ResultResponse getChattingFileListByRoom(Long chattingRoomId, int page, int size, Integer totalRecord) {
        log.info("=======================");
        log.info("CHATTING-MESSAGE-SERVICE: [getChattingFileListByRoom] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] ChattingMessage list 가져와서 정보 담기
        if (totalRecord == null) {
            // 페이지네이션 전, total page 구하기
            totalRecord = chattingMessageRepository.findAllFileUrlCountByChattingRoomOrderByChattingMessageId(foundChattingRoom.getChattingRoomId());
        }
        ScrollPagination scrollPagination = new ScrollPaginationFile(size, page, totalRecord);
        List<ChattingMessage> chattingMessageList = chattingMessageRepository
                .findAllFileUrlByChattingRoom(foundChattingRoom.getChattingRoomId(), scrollPagination.getStartNum(), scrollPagination.getPageSize());
        List<ChattingMessageGetResponse> chattingMessageGetResponseList = new ArrayList<>();
        chattingMessageList.forEach(c -> chattingMessageGetResponseList.add(toChattingMessageResponse.toChattingMessageGetResponse(c)));

        ChattingMessageGetListPaginationRespnse chattingMessageGetListPaginationRespnse = toChattingMessageResponse.toChattingMessageGetListPaginationResponse(chattingMessageGetResponseList, scrollPagination);

        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_FILE_LIST_GET_SUCCESS, chattingMessageGetListPaginationRespnse);

        log.info("CHATTING-MESSAGE-SERVICE: [getChattingFileListByRoom] END");
        log.info("=======================");

        return resultResponse;
    }

    @Transactional
    public ResultResponse updateChattingMessage(Long accountId, Long chattingRoomId, Long chattingMessageId, ChattingMessageContentCreateRequest chattingMessageContentCreateRequest) {
        log.info("=======================");
        log.info("CHATTING-MESSAGE-SERVICE: [updateChattingMessage] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] 채팅 sender 유효성 검사
        Account account = accountRepository.findByAccountIdAndIsDeletedFalse(accountId).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [3] 채팅 message 유효성 검사
        ChattingMessage chattingMessage = chattingMessageRepository.findByIdAndRoomIdAndAccountIdAndIsDeletedFalse(foundChattingRoom.getChattingRoomId(), accountId, chattingMessageId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.CHATTING_MESSAGE_NOT_FOUND, CustomExceptionMessage.CHATTING_MESSAGE_NOT_FOUND.getMessage())
                ); // 삭제한 메시지는 수정할 수 없도록 is_deleted == false인 메시지만 조회

        // [4] 채팅 메시지 수정 및 정보 담기
        chattingMessage.setContent(chattingMessageContentCreateRequest.getContent());
        chattingMessage.setIsEdited(Boolean.TRUE);
        ChattingMessage savedChattingMessage = chattingMessageRepository.save(chattingMessage);
        ChattingMessageGetResponse chattingMessageUpdateResponse = toChattingMessageResponse.toChattingMessageGetResponse(savedChattingMessage);

        // [5] 채팅방 id, 채팅 sender id, 채팅 메시지 정보가 담긴 chattingMessageCreateResponse
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_UPDATE_SUCCESS, chattingMessageUpdateResponse);

        log.info("CHATTING-MESSAGE-SERVICE: [updateChattingMessage] END");
        log.info("=======================");

        return resultResponse;
    }

    @Transactional
    public ResultResponse deleteChattingMessage(Long accountId, Long chattingRoomId, Long chattingMessageId) {
        log.info("=======================");
        log.info("CHATTING-MESSAGE-SERVICE: [deleteChattingMessage] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] 채팅 sender 유효성 검사
        Account account = accountRepository.findByAccountIdAndIsDeletedFalse(accountId).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [3] 채팅 message 유효성 검사
        chattingMessageRepository.findByIdAndRoomIdAndAccountIdAndIsDeletedFalse(foundChattingRoom.getChattingRoomId(), accountId, chattingMessageId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.CHATTING_MESSAGE_NOT_FOUND, CustomExceptionMessage.CHATTING_MESSAGE_NOT_FOUND.getMessage())
                ); // 삭제한 메시지는 다시 삭제할 수 없도록 is_deleted == false인 메시지만 조회

        // [4] 채팅 메시지 삭제
        chattingMessageRepository.deleteByIdAndRoomIdAndAccountIdAndIsDeletedFalse(foundChattingRoom.getChattingRoomId(), accountId, chattingMessageId);

        // [5] 채팅방 id, 채팅 sender id, 채팅 메시지 정보가 담긴 chattingMessageCreateResponse
        ResultResponse resultResponse = new ResultResponse(ResultCode.CHATTING_MESSAGE_DELETE_SUCCESS, "delete success");

        log.info("CHATTING-MESSAGE-SERVICE: [deleteChattingMessage] END");
        log.info("=======================");

        return resultResponse;
    }

    @Transactional
    public ResponseEntity<byte[]> downloadChattingFile(Long accountId, Long chattingRoomId, Long chattingMessageId) {
        log.info("=======================");
        log.info("CHATTING-MESSAGE-SERVICE: [downloadChattingFile] START");

        // [1] 채팅방 유효성 검사
        ChattingRoom foundChattingRoom = chattingRoomRepository.findByChattingRoomIdAndIsDeletedFalse(chattingRoomId).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_ROOM_NOT_FOUND, CustomExceptionMessage.CHATTING_ROOM_NOT_FOUND.getMessage())
        );

        // [2] 채팅 sender 유효성 검사
        Account account = accountRepository.findByAccountIdAndIsDeletedFalse(accountId).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        chattingParticipantRepository.findByAccountAndChattingRoomAndIsDeletedFalse(account, foundChattingRoom).orElseThrow(
                () -> new BusinessException(ErrorCode.CHATTING_PARTICIPANT_NOT_FOUND, CustomExceptionMessage.CHATTING_PARTICIPANT_NOT_FOUND.getMessage())
        );

        // [3] 채팅 message 유효성 검사
        ChattingMessage chattingMessage = chattingMessageRepository.findByIdAndRoomIdAndAccountIdAndIsDeletedFalse(foundChattingRoom.getChattingRoomId(), accountId, chattingMessageId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.CHATTING_MESSAGE_NOT_FOUND, CustomExceptionMessage.CHATTING_MESSAGE_NOT_FOUND.getMessage())
                );

        log.info("CHATTING-MESSAGE-SERVICE: [downloadChattingFile] END");
        log.info("=======================");

        // [4] 채팅 파일 다운로드
        try {
            return s3FileService.downloadFile(chattingMessage.getFileUrl());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.CHATTING_FILE_ERROR, CustomExceptionMessage.CHATTING_FILE_DOWNLOAD_FAILED.getMessage());
        }
    }

}
