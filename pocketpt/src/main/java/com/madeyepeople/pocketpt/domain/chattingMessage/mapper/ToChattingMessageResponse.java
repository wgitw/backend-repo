package com.madeyepeople.pocketpt.domain.chattingMessage.mapper;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.*;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingMessage.repositoryInterface.ChattingMessageWithBookmarkInterface;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.global.common.ScrollPagination;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ToChattingMessageResponse {
    public ChattingMessageCreateResponse toChattingMessageCreateResponse(ChattingRoom chattingRoom, Account account, ChattingMessage chattingMessage) {
        return ChattingMessageCreateResponse.builder()
                .chattingRoomId(chattingRoom.getChattingRoomId())
                .chattingAccountId(account.getAccountId())
                .chattingAccountName(account.getName())
                .chattingAccountProfilePictureUrl(account.getProfilePictureUrl())
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .content(chattingMessage.getContent())
                .isEdited(chattingMessage.getIsEdited())
                .notViewCount(chattingMessage.getNotViewCount())
                .createdAt(chattingMessage.getCreatedAt())
                .build();
    }

    public ChattingMessageCreateResponse toChattingFileCreateResponse(ChattingRoom chattingRoom, Account account, ChattingMessage chattingMessage) {
        return ChattingMessageCreateResponse.builder()
                .chattingRoomId(chattingRoom.getChattingRoomId())
                .chattingAccountId(account.getAccountId())
                .chattingAccountName(account.getName())
                .chattingAccountProfilePictureUrl(account.getProfilePictureUrl())
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .fileUrl(chattingMessage.getFileUrl())
                .isEdited(chattingMessage.getIsEdited())
                .notViewCount(chattingMessage.getNotViewCount())
                .createdAt(chattingMessage.getCreatedAt())
                .build();
    }

    public ChattingMessageGetResponse toChattingMessageGetResponse(ChattingMessage chattingMessage) {
        ChattingRoom chattingRoom = chattingMessage.getChattingParticipant().getChattingRoom();
        Account account = chattingMessage.getChattingParticipant().getAccount();
        return ChattingMessageGetResponse.builder()
                .chattingRoomId(chattingRoom.getChattingRoomId())
                .chattingAccountId(account.getAccountId())
                .chattingAccountName(account.getName())
                .chattingAccountProfilePictureUrl(account.getProfilePictureUrl())
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .content(chattingMessage.getContent())
                .fileUrl(chattingMessage.getFileUrl())
                .isEdited(chattingMessage.getIsEdited())
                .notViewCount(chattingMessage.getNotViewCount())
                .isDeleted(chattingMessage.getIsDeleted())
                .createdAt(chattingMessage.getCreatedAt())
                .updatedAt(chattingMessage.getUpdatedAt())
                .build();
    }

    public ChattingMessageWithBookmarkGetResponse toChattingMessageWithBookmarkGetResponse(ChattingMessageWithBookmarkInterface chattingMessage) {
        return ChattingMessageWithBookmarkGetResponse.builder()
                .chattingRoomId(chattingMessage.getChattingRoomId())
                .chattingAccountId(chattingMessage.getAccountId())
                .chattingAccountName(chattingMessage.getNickname())
                .chattingAccountProfilePictureUrl(chattingMessage.getProfilePictureUrl())
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .content(chattingMessage.getContent())
                .fileUrl(chattingMessage.getFileUrl())
                .isBookmarked(chattingMessage.getIsBookmarked()==1?Boolean.TRUE:Boolean.FALSE)
                .isEdited(chattingMessage.getIsEdited())
                .notViewCount(chattingMessage.getNotViewCount())
                .isDeleted(chattingMessage.getIsDeleted())
                .createdAt(chattingMessage.getCreatedAt())
                .updatedAt(chattingMessage.getUpdatedAt())
                .build();
    }

    public ChattingMessageGetResponse toChattingMessageGetResponseForUpdateChattingRoomList(ChattingMessage chattingMessage, ChattingParticipant chattingParticipant) {
        ChattingRoom chattingRoom = chattingMessage.getChattingParticipant().getChattingRoom();
        Account account = chattingMessage.getChattingParticipant().getAccount();

        return ChattingMessageGetResponse.builder()
                .chattingRoomId(chattingRoom.getChattingRoomId())
                .chattingAccountId(account.getAccountId())
                .chattingAccountName(account.getName())
                .chattingAccountProfilePictureUrl(account.getProfilePictureUrl())
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .content(chattingMessage.getContent())
                .fileUrl(chattingMessage.getFileUrl())
                .isEdited(chattingMessage.getIsEdited())
                .notViewCount(chattingParticipant.getNotViewCount())
                .isDeleted(chattingMessage.getIsDeleted())
                .createdAt(chattingMessage.getCreatedAt())
                .updatedAt(chattingMessage.getUpdatedAt())
                .build();
    }

    public ChattingMessageGetResponseForCreateRoom toChattingMessageGetResponseForRoom(ChattingRoom chattingRoom, ChattingParticipant hostCattingParticipant) {
        Account account = hostCattingParticipant.getAccount();

        return ChattingMessageGetResponseForCreateRoom.builder()
                .chattingRoomId(chattingRoom.getChattingRoomId())
                .hostChattingAccountId(account.getAccountId())
                .hostChattingAccountName(account.getName())
                .hostChattingAccountProfilePictureUrl(account.getProfilePictureUrl())
                .createdAt(chattingRoom.getCreatedAt())
                .updatedAt(chattingRoom.getUpdatedAt())
                .build();
    }

    public ChattingMessageGetListPaginationRespnse toChattingMessageGetListPaginationResponse(List<ChattingMessageGetResponse> chattingMessageGetResponseList,
                                                                                              ScrollPagination scrollPagination) {
        return ChattingMessageGetListPaginationRespnse.builder()
                .chattingMessageGetResponseList(chattingMessageGetResponseList)
                .pageSize(scrollPagination.getPageSize())
                .pageNum(scrollPagination.getPageNum())
                .totalRecord(scrollPagination.getTotalRecord())
                .hasPreviousPage(scrollPagination.isHasPreviousPage())
                .hasNextPage(scrollPagination.isHasNextPage())
                .build();
    }

    public ChattingMessageWithBookmarkGetListPaginationRespnse toChattingMessageWithBookmarkGetListPaginationResponse(List<ChattingMessageWithBookmarkGetResponse> chattingMessageGetResponseList,
                                                                                              ScrollPagination scrollPagination) {
        return ChattingMessageWithBookmarkGetListPaginationRespnse.builder()
                .chattingMessageWithBookmarkGetResponses(chattingMessageGetResponseList)
                .pageSize(scrollPagination.getPageSize())
                .pageNum(scrollPagination.getPageNum())
                .totalRecord(scrollPagination.getTotalRecord())
                .hasPreviousPage(scrollPagination.isHasPreviousPage())
                .hasNextPage(scrollPagination.isHasNextPage())
                .build();
    }


}
