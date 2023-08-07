package com.madeyepeople.pocketpt.domain.chattingMessageBookmark.dto.response;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.response.ChattingMessageGetResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChattingMessageBookmarkGetListPaginationResponse {
    private List<ChattingMessageGetResponse> chattingMessageBookmarkCreateResponseList;
    private int pageSize; // 한 페이지 당 가져올 데이터 개수
    private int pageNum; // 페이지 번호
    private boolean hasPreviousPage; // 이전페이지
    private boolean hasNextPage; // 다음페이지
}
