package com.madeyepeople.pocketpt.domain.chattingRoom.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
@Builder
public class ChattingRoomListPaginationResponse {
    private List<ChattingRoomGetResponse> chattingRoomResponseList;
    private Pageable pageable;
    private int number;
    private int numberOfElements;
    private Boolean first;
    private Boolean last;
    private Boolean empty;
}
