package com.madeyepeople.pocketpt.domain.chattingMessage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChattingMessageGetListPaginationRespnse {
    private List<ChattingMessageGetResponse> chattingMessageGetResponseList;
    private int pageSize; // 한 페이지 당 가져올 데이터 개수
    private int pageNum; // 페이지 번호
    private long totalRecord; // 총 게시물(레코드) 갯수
    private boolean hasPreviousPage; // 이전페이지
    private boolean hasNextPage; // 다음페이지
}
