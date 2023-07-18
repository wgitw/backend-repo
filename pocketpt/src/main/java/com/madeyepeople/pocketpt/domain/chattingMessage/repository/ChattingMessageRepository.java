package com.madeyepeople.pocketpt.domain.chattingMessage.repository;

import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {
//    Optional<ChattingMessage> findByChattingMessageIdAndIsDeletedFalse();
//
//    Optional<ChattingMessage> findByChattingMessageIdAndIsDeletedFalse();

    // @Query("SELECT m FROM chatting_message m WHERE m.chatting_room = :chattingRoom ORDER BY m.created_at DESC LIMIT 1") // 이건 안됨, 아마 limit 1이 안되는거 같음
    @Query(value="SELECT * FROM chatting_message m WHERE m.chatting_room = :chattingRoom ORDER BY m.created_at DESC LIMIT 1", nativeQuery = true)
    Optional<ChattingMessage> findLatestChattingMessageByRoom(@Param("chattingRoom") Long chattingRoom);

    @Query(value = "SELECT * FROM chatting_message m WHERE m.chatting_room = :chattingRoom", nativeQuery = true)
    List<ChattingMessage> findAllByChattingRoom(@Param("chattingRoom") Long chattingRoom);

    @Query(value = "SELECT * FROM chatting_message m WHERE m.chatting_room = 11 AND m.file_url IS NOT NULL", nativeQuery = true)
    List<ChattingMessage> findAllFileUrlByChattingRoom(@Param("chattingRoom") Long chattingRoom);

    //Optional<ChattingMessage> findByChattingRoomOrderByCreatedAtDesc(Long chattingRoom);
}
