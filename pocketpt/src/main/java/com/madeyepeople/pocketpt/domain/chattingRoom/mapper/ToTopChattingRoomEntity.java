package com.madeyepeople.pocketpt.domain.chattingRoom.mapper;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.TopChattingRoom;
import org.springframework.stereotype.Component;

@Component
public class ToTopChattingRoomEntity {
    public TopChattingRoom toTopChattingRoomCreateEntity(ChattingRoom chattingRoom, Account account) {
        return TopChattingRoom.builder()
                .chattingRoom(chattingRoom)
                .account(account)
                .build();
    }
}
