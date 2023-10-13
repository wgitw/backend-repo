package com.madeyepeople.pocketpt.domain.chattingMessageBookmark.mapper;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageGetResponse;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingMessage.mapper.ToChattingMessageResponse;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.dto.response.ChattingMessageBookmarkCreateResponse;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.dto.response.ChattingMessageBookmarkGetListPaginationResponse;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.entity.ChattingMessageBookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ToChattingMessageBookmarkResponse {
    private final ToChattingMessageResponse toChattingMessageResponse;

    public ChattingMessageBookmarkCreateResponse toChattingMessageBookmarkCreateResponse(ChattingMessageBookmark chattingMessageBookmark) {
        ChattingMessage chattingMessage = chattingMessageBookmark.getChattingMessage();
        ChattingMessageGetResponse chattingMessageGetResponse = toChattingMessageResponse.toChattingMessageGetResponse(chattingMessage);
        return ChattingMessageBookmarkCreateResponse.builder()
                .chattingMessageId(chattingMessageBookmark.getChattingMessage().getChattingMessageId())
                .chattingRoomId(chattingMessageBookmark.getChattingRoom().getChattingRoomId())
                .chattingMessageGetResponse(chattingMessageGetResponse)
                .createdAt(chattingMessageBookmark.getCreatedAt())
                .build();
    }

    public ChattingMessageBookmarkGetListPaginationResponse toChattingMessageBookmarkGetListPaginationResponse(
                                        List<ChattingMessageGetResponse> chattingMessageBookmarkCreateResponseList,
                                        Slice<ChattingMessageBookmark> chattingMessageBookmarkList) {
        return ChattingMessageBookmarkGetListPaginationResponse.builder()
                .chattingMessageWithBookmarkGetResponses(chattingMessageBookmarkCreateResponseList)
                .pageSize(chattingMessageBookmarkList.getSize())
                .pageNum(chattingMessageBookmarkList.getNumber())
                .hasPreviousPage(chattingMessageBookmarkList.hasPrevious())
                .hasNextPage(chattingMessageBookmarkList.hasNext())
                .build();
    }
}
