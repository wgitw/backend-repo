package com.madeyepeople.pocketpt.domain.chattingParticipant.repository;

import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingParticipantRepository extends JpaRepository<ChattingParticipant, Long> {
}
