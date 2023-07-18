package com.madeyepeople.pocketpt.domain.chattingParticipant.repository;

import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChattingParticipantRepository extends JpaRepository<ChattingParticipant, Long> {
    Optional<ChattingParticipant> findByChattingParticipantId(Long chattingParticipantId);
    List<ChattingParticipant> findAllByParticipantId(Long accountId);

    // List<ChattingParticipant> findAllByParticipantIdAndIsDeletedFalse(Long accountId, Pageable pageable);

    Slice<ChattingParticipant> findAllByParticipantIdAndIsDeletedFalse(Long accountId, Pageable pageable);
}
