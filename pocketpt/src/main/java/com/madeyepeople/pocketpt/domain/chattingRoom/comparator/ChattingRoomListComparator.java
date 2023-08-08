package com.madeyepeople.pocketpt.domain.chattingRoom.comparator;

import com.madeyepeople.pocketpt.domain.chattingRoom.dto.response.ChattingRoomGetResponse;

import java.time.LocalDateTime;
import java.util.Comparator;

public class ChattingRoomListComparator implements Comparator<ChattingRoomGetResponse> {
    @Override
    public int compare(ChattingRoomGetResponse chattingRoomGetResponse1,
                       ChattingRoomGetResponse chattingRoomGetResponse2) {
        LocalDateTime localDateTime1 = chattingRoomGetResponse1.getLatestChattingMessageCreatedAt();
        if (localDateTime1 == null) {
            localDateTime1 = chattingRoomGetResponse1.getCreatedAt();
        }

        LocalDateTime localDateTime2 = chattingRoomGetResponse2.getLatestChattingMessageCreatedAt();
        if (localDateTime2 == null) {
            localDateTime2 = chattingRoomGetResponse2.getCreatedAt();
        }

        return localDateTime1.compareTo(localDateTime2);
    }
}
