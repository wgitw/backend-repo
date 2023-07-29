package com.madeyepeople.pocketpt.domain.chattingRoom.repository;

import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
    Optional<ChattingRoom> findByChattingRoomIdAndIsDeletedFalse(Long chattingRoomId);
}
