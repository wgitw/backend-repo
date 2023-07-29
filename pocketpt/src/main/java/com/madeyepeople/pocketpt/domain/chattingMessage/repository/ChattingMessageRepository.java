package com.madeyepeople.pocketpt.domain.chattingMessage.repository;

import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {

    // select - is_deleted = true여도 "삭제된 메시지입니다"로 보여줘야 해서 내보내야 함
    @Query(value=
            """
                SELECT *
                FROM chatting_message m
                WHERE m.chatting_room = :chattingRoom
                    AND ORDER BY m.created_at DESC LIMIT 1
            """, nativeQuery = true)
    Optional<ChattingMessage> findLatestChattingMessageByRoom(Long chattingRoom);

    @Query(value =
            """
                SELECT *
                FROM chatting_message m
                WHERE m.chatting_room = :chattingRoom
                    ORDER BY chatting_message_id DESC
            """, nativeQuery = true)
    List<ChattingMessage> findAllByChattingRoomOrderByChattingMessageIdDesc(Long chattingRoom);

    @Query(value =
            """
                SELECT *
                FROM chatting_message m
                WHERE m.chatting_room = :chattingRoom
                    AND m.file_url IS NOT NULL
                    ORDER BY chatting_message_id DESC
            """, nativeQuery = true)
    List<ChattingMessage> findAllFileUrlByChattingRoom(Long chattingRoom);

    // update
    @Modifying
    @Query(value =
            """
                UPDATE chatting_message m
                SET m.not_view_count = m.not_view_count - 1
                WHERE m.chatting_room = :chattingRoomId
                    AND m.account NOT IN (:accountId)
                    AND m.not_view_count > 0
            """, nativeQuery = true)
    int updateAllByNotViewCountMinusOneByRoomIdAndChattingAccountId(Long chattingRoomId, Long accountId);

    // delete - TODO: is_deleted = true와 content 내용 null 처리 진행할 것
}
