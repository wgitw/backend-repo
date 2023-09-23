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

    // 채팅방의 가장 최근 메시지
    @Query(value=
            """
                SELECT *
                FROM chatting_message m
                WHERE m.chatting_room = :chattingRoom
                    ORDER BY m.created_at DESC LIMIT 1
            """, nativeQuery = true)
    Optional<ChattingMessage> findLatestChattingMessageByRoom(Long chattingRoom);

    // 삭제되지 않은 메시지
    @Query(value=
            """
                SELECT *
                FROM chatting_message m
                WHERE m.chatting_room = :chattingRoom
                    AND m.chatting_message_id = :chattingMessage
                    AND account = :account
                    AND is_deleted = FALSE
            """, nativeQuery = true)
    Optional<ChattingMessage> findByIdAndRoomIdAndAccountIdAndIsDeletedFalse(Long chattingRoom, Long account, Long chattingMessage);

    // 채팅방의 메시지 총개수
    @Query(value =
            """
                SELECT count(*)
                FROM chatting_message m
                WHERE m.chatting_room = :chattingRoom
                    AND 0 < m.chatting_message_id
                    ORDER BY chatting_message_id DESC
            """, nativeQuery = true)
    int findAllCountByChattingRoomOrderByChattingMessageId(Long chattingRoom);

    // 채팅방의 메시지 리스트 가져오기
    @Query(value =
            """
                SELECT *
                FROM
                    (
                        SELECT *
                        FROM chatting_message m
                        WHERE m.chatting_room = :chattingRoom
                    ) cm
                ORDER BY cm.chatting_message_id DESC
                LIMIT :startNum, :pageSize
            """, nativeQuery = true)
    List<ChattingMessage> findAllByChattingRoomOrderByChattingMessageIdDesc(Long chattingRoom, int startNum, int pageSize);

    // 채팅방의 파일 총개수 - is_deleted == True여도 삭제된 파일입니다로 보여주는 것을 의도하여 내보냄
    @Query(value =
            """
                SELECT count(*)
                FROM chatting_message m
                WHERE m.chatting_room = :chattingRoom
                    AND m.file_url IS NOT NULL
                    AND 0 < m.chatting_message_id
                    ORDER BY chatting_message_id DESC
            """, nativeQuery = true)
    int findAllFileUrlCountByChattingRoomOrderByChattingMessageId(Long chattingRoom);

    // 채팅방의 파일 리스트 - is_deleted == True여도 삭제된 파일입니다로 보여주는 것을 의도하여 내보냄
    @Query(value =
            """
                SELECT *
                FROM chatting_message m
                WHERE m.chatting_room = :chattingRoom
                    AND m.file_url IS NOT NULL
                    ORDER BY m.chatting_message_id DESC
                    LIMIT :pageSize OFFSET :startNum
            """, nativeQuery = true)
    List<ChattingMessage> findAllFileUrlByChattingRoom(Long chattingRoom, int startNum, int pageSize);

    // update

    // 읽은 메시지로 업데이트
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

    // delete
    @Modifying
    @Query(value =
            """
                UPDATE chatting_message m
                SET
                    m.is_deleted = TRUE,
                    m.content = "삭제된 메시지입니다.",
                    m.file_url = NULL
                WHERE m.chatting_room = :chattingRoom
                    AND m.chatting_message_id = :chattingMessage
                    AND account = :account
                    AND m.is_deleted = FALSE
            """, nativeQuery = true)
    int deleteByIdAndRoomIdAndAccountIdAndIsDeletedFalse(Long chattingRoom, Long account, Long chattingMessage);
}
