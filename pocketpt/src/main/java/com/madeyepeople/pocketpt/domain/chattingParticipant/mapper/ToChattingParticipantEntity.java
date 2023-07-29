package com.madeyepeople.pocketpt.domain.chattingParticipant.mapper;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.stereotype.Component;


@Component
public class ToChattingParticipantEntity {

    public ChattingParticipant toChattingHostCreateEntity(ChattingRoom chattingRoom, Account account) {
        return ChattingParticipant.builder()
                .chattingRoom(chattingRoom)
                .account(account)
                .isHost(Boolean.TRUE)
                .build();
    }

    public ChattingParticipant toChattingParticipantCreateEntity(ChattingRoom chattingRoom, Account account) {
        return ChattingParticipant.builder()
                .chattingRoom(chattingRoom)
                .account(account)
                .isHost(Boolean.FALSE)
                .build();
    }
}
