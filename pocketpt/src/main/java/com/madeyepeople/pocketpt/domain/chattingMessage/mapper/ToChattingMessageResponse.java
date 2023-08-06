package com.madeyepeople.pocketpt.domain.chattingMessage.mapper;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageCreateResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageGetListPaginationRespnse;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageGetResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
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
                .chattingAccountName(account.getNickname())
                .chattingAccountProfilePictureUrl(account.getProfilePictureUrl())
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .content(chattingMessage.getContent())
                .isEdited(chattingMessage.getIsEdited())
                .isBookmarked(chattingMessage.getIsBookmarked())
                .notViewCount(chattingMessage.getNotViewCount())
                .createdAt(chattingMessage.getCreatedAt())
                .build();
    }

    public ChattingMessageCreateResponse toChattingFileCreateResponse(ChattingRoom chattingRoom, Account account, ChattingMessage chattingMessage) {
        return ChattingMessageCreateResponse.builder()
                .chattingRoomId(chattingRoom.getChattingRoomId())
                .chattingAccountId(account.getAccountId())
                .chattingAccountName(account.getNickname())
                .chattingAccountProfilePictureUrl(account.getProfilePictureUrl())
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .fileUrl(chattingMessage.getFileUrl())
                .isEdited(chattingMessage.getIsEdited())
                .isBookmarked(chattingMessage.getIsBookmarked())
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
                .chattingAccountName(account.getNickname())
                .chattingAccountProfilePictureUrl(account.getProfilePictureUrl())
                .chattingMessageId(chattingMessage.getChattingMessageId())
                .content(chattingMessage.getContent())
                .fileUrl(chattingMessage.getFileUrl())
                .isEdited(chattingMessage.getIsEdited())
                .isBookmarked(chattingMessage.getIsBookmarked())
                .notViewCount(chattingMessage.getNotViewCount())
                .isDeleted(chattingMessage.getIsDeleted())
                .createdAt(chattingMessage.getCreatedAt())
                .updatedAt(chattingMessage.getUpdatedAt())
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

}
