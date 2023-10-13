package com.madeyepeople.pocketpt.domain.chattingMessageBookmark.repository;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.entity.ChattingMessageBookmark;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChattingMessageBookmarkRepository extends JpaRepository<ChattingMessageBookmark, Long> {
    Slice<ChattingMessageBookmark> findAllByAccountAndChattingRoom(Account account, ChattingRoom chattingRoom, Pageable pageable);

    Optional<ChattingMessageBookmark> findByChattingMessageAndChattingRoomAndAccount(ChattingMessage chattingMessage, ChattingRoom foundChattingRoom, Account account);

    List<ChattingMessageBookmark> findAllByChattingMessageAndChattingRoom(ChattingMessage chattingMessage, ChattingRoom foundChattingRoom);

    @Modifying
    void deleteByChattingMessageAndChattingRoom(ChattingMessage chattingMessage, ChattingRoom foundChattingRoom);

}
