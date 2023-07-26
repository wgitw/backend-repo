package com.madeyepeople.pocketpt.domain.chattingParticipant.repository;

import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChattingParticipantRepository extends JpaRepository<ChattingParticipant, Long> {
    // update
    @Modifying
    @Query(value = "UPDATE chatting_participant SET not_view_count = 0 WHERE chatting_room_id = :chattingRoomId AND chatting_participant_id = (:chattingParticipantId) AND is_deleted = FALSE", nativeQuery = true)
    int updateAllByNotViewCountZeroByRoomIdAndChattingParticipantIdAndIsDeletedFalse(Long chattingRoomId, Long chattingParticipantId);

    // select
    Optional<ChattingParticipant> findByChattingParticipantIdAndIsDeletedFalse(Long chattingParticipantId);

    @Query(value="SELECT m.chatting_participant_id, m.created_at, m.is_deleted, m.updated_at, m.participant_account_id, m.chatting_room_entry_time, " +
            "m.chatting_room_exit_time, m.is_host, m.chatting_room_id, m.simp_session_id, m.not_view_count FROM chatting_participant m JOIN account a WHERE m.chatting_room_id = :chattingRoomId AND a.email = :accountUsername " +
            "AND m.participant_account_id = a.account_id AND m.is_deleted = FALSE AND a.is_deleted = FALSE", nativeQuery = true)
    Optional<ChattingParticipant> findByAccountUsernameAndChattingRoomIdAndIsDeletedFalse(String accountUsername, Long chattingRoomId);

    @Query(value="SELECT * FROM chatting_participant m WHERE m.chatting_room_id = :chattingRoomId AND m.participant_account_id = :accountId AND m.is_deleted = FALSE", nativeQuery = true)
    Optional<ChattingParticipant> findByAccountIdAndChattingRoomIdAndIsDeletedFalse(Long accountId, Long chattingRoomId);

    Optional<ChattingParticipant> findBySimpSessionIdAndIsDeletedFalse(String simpSessionId);

    @Query(value="SELECT * FROM chatting_participant m WHERE chatting_room_id = :chattingRoomId AND m.participant_account_id NOT IN (:accountId) AND m.is_deleted = FALSE", nativeQuery = true)
    List<ChattingParticipant> findAllByChattingRoomIdAndNotInAccountIdAndIsDeletedFalse(Long chattingRoomId, Long accountId);

    Slice<ChattingParticipant> findAllByAccountIdAndIsDeletedFalse(Long accountId, Pageable pageable);
}
