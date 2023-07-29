package com.madeyepeople.pocketpt.domain.chattingParticipant.repository;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChattingParticipantRepository extends JpaRepository<ChattingParticipant, Long> {
    // select
    Optional<ChattingParticipant> findByAccountAndChattingRoomAndIsDeletedFalse(Account account, ChattingRoom chattingRoom);

    Optional<ChattingParticipant> findBySimpSessionIdAndIsDeletedFalse(String simpSessionId);

    @Query(value=
            """
                SELECT *
                FROM chatting_participant m
                WHERE chatting_room_id = :chattingRoomId
                    AND m.account_id NOT IN (:accountId)
                    AND m.is_deleted = FALSE
            """, nativeQuery = true)
    List<ChattingParticipant> findAllByChattingRoomIdAndNotInAccountIdAndIsDeletedFalse(Long chattingRoomId, Long accountId);

    // update
    @Modifying
    @Query(value =
            """
                UPDATE chatting_participant
                SET not_view_count = 0
                WHERE chatting_room_id = :chattingRoomId
                    AND account_id = (:accountId)
                    AND is_deleted = FALSE
            """, nativeQuery = true)
    int updateAllByNotViewCountZeroByRoomIdAndAccountIdAndIsDeletedFalse(Long chattingRoomId, Long accountId);

    // delete
}
