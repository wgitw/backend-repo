package com.madeyepeople.pocketpt.domain.chattingRoom.repository;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.TopChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopChattingRoomRepository extends JpaRepository<TopChattingRoom, Long> {
    Optional<TopChattingRoom> findByChattingRoomAndAccountAndIsDeletedFalse(ChattingRoom chattingRoom, Account account);

    Long countByAccountAndIsDeletedFalse(Account account);

    List<TopChattingRoom> findByAccountAndIsDeletedFalseOrderByCreatedAtDesc(Account account);
}
