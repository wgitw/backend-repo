package com.madeyepeople.pocketpt.domain.ptMatching.repository;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatus;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PtMatchingRepository extends JpaRepository<PtMatching, Long> {
    List<PtMatching> findAllByTrainerAccountIdAndIsDeletedFalse(Long trainerAccountId);
    List<PtMatching> findAllByTraineeAccountIdAndIsDeletedFalse(Long traineeAccountId);
    List<PtMatching> findAllByTrainerAccountIdAndIsDeletedFalseAndStatus(Long trainerAccountId, PtStatus status);
    List<PtMatching> findAllByTraineeAccountIdAndIsDeletedFalseAndStatus(Long traineeAccountId, PtStatus status);
    Optional<PtMatching> findByPtMatchingIdAndIsDeletedFalse(Long ptMatchingId);
}
