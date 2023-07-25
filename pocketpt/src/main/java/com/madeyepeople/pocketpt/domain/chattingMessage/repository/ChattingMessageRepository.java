package com.madeyepeople.pocketpt.domain.chattingMessage.repository;

import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {
    // update
    @Modifying
    @Query(value = "UPDATE chatting_message m SET m.not_view_count = m.not_view_count - 1 WHERE m.chatting_room = :chattingRoomId AND m.chatting_participant_id NOT IN (:chattingParticipantId) AND m.not_view_count > 0 AND m.is_deleted = FALSE", nativeQuery = true)
    int updateAllByNotViewCountMinusOneByRoomIdAndChattingParticipantIdAndIsDeletedFalse(Long chattingRoomId, Long chattingParticipantId);

    // select
    @Query(value="SELECT * FROM chatting_message m WHERE m.chatting_room = :chattingRoom ORDER BY m.created_at DESC LIMIT 1", nativeQuery = true)
    Optional<ChattingMessage> findLatestChattingMessageByRoom(@Param("chattingRoom") Long chattingRoom);

    @Query(value = "SELECT * FROM chatting_message m WHERE m.chatting_room = :chattingRoom", nativeQuery = true)
    List<ChattingMessage> findAllByChattingRoom(@Param("chattingRoom") Long chattingRoom);

    @Query(value = "SELECT * FROM chatting_message m WHERE m.chatting_room = 11 AND m.file_url IS NOT NULL", nativeQuery = true)
    List<ChattingMessage> findAllFileUrlByChattingRoom(@Param("chattingRoom") Long chattingRoom);

}
