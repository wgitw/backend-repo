package com.madeyepeople.pocketpt.domain.chattingMessageBookmark.mapper;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.entity.ChattingMessageBookmark;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.stereotype.Component;

@Component
public class ToChattingMessageBookmarkEntity {
    public ChattingMessageBookmark toChattingMessageBookmarkEntity(ChattingMessage chattingMessage, ChattingRoom chattingRoom, Account account) {
        return ChattingMessageBookmark.builder()
                .chattingMessage(chattingMessage)
                .chattingRoom(chattingRoom)
                .account(account)
                .build();
    }

}
