package com.madeyepeople.pocketpt.domain.ptMatching.service;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatus;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.PtMatchingSummary;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.request.PtRegistrationRequest;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.response.PtRegistrationResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtMatchingEntity;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtMatchingListResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtMatchingSummary;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtRegistrationResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.repository.PtMatchingRepository;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class PtMatchingService {

    private final AccountRepository accountRepository;
    private final PtMatchingRepository ptMatchingRepository;

    private final ToPtMatchingEntity toPtMatchingEntity;
    private final ToPtRegistrationResponse toPtRegistrationResponse;
    private final ToPtMatchingListResponse toPtMatchingListResponse;
    private final ToPtMatchingSummary toPtMatchingSummary;

    private final SecurityUtil securityUtil;

    @Transactional
    public PtRegistrationResponse registerPt(PtRegistrationRequest ptRegistrationRequest) throws ConstraintViolationException, BusinessException {
        // TODO:  중복 등록 불가하게 수정 (trainerId, traineeId, status = pending unique하게 변경)
        Optional<Account> trainer = accountRepository.findByIdentificationCodeAndIsDeletedFalse(ptRegistrationRequest.getTrainerCode());

        // 해당 Identification Code 가진 account가 있는지, 있다면 Role = trainer인지 확인
        if (trainer.isEmpty()) {
            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.TRAINER_IDENTIFICATION_CODE_NOT_FOUND.getMessage());
        } else if (!trainer.get().getAccountRole().getValue().equals("trainer")) {
            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.IDENTIFICATION_CODE_IS_NOT_TRAINER.getMessage());
        }

        Account trainee = securityUtil.getLoginAccountEntity();

        // TODO: subscriptionPeriod로 expiredDate도 계산해야 함
        PtMatching saved = ptMatchingRepository.save(toPtMatchingEntity.fromAccountEntities(trainer.get(), trainee, ptRegistrationRequest.getSubscriptionPeriod()));
        log.error("saved: {}", saved.toString());
        // TODO: ResultResponse 사용하도록 변경, Account 로직들도 마찬가지
        return toPtRegistrationResponse.fromPtMatchingEntity(saved);
    }

    @Transactional
    public ResultResponse getPtMatchingList(String mode) {
        Account account = securityUtil.getLoginAccountEntity();
        List<PtMatching> ptMatchingList;

        if (account.getAccountRole().equals(Role.TRAINER)) {
            if (mode.equals("all")) {
                ptMatchingList = ptMatchingRepository.findAllByTrainerAccountIdAndIsDeletedFalse(account.getAccountId());
            } else {
                ptMatchingList = ptMatchingRepository.findAllByTrainerAccountIdAndIsDeletedFalseAndStatus(account.getAccountId(), PtStatus.valueOf(mode.toUpperCase()));
            }
        } else {
            if (mode.equals("all")) {
                ptMatchingList = ptMatchingRepository.findAllByTraineeAccountIdAndIsDeletedFalse(account.getAccountId());
            } else {
                ptMatchingList = ptMatchingRepository.findAllByTraineeAccountIdAndIsDeletedFalseAndStatus(account.getAccountId(), PtStatus.valueOf(mode.toUpperCase()));
            }
        }

        // 내 accountId를 이용해 PtMatching 정보들 중, 상대방 정보만 추출
        List<PtMatchingSummary> ptMatchingSummaryList = toPtMatchingListResponse.fromPtMatchingEntityList(ptMatchingList, account.getAccountId());

        return ResultResponse.of(ResultCode.PT_MATCHING_LIST_GET_SUCCESS, ptMatchingSummaryList);
    }

    @Transactional
    public ResultResponse acceptPtMatching(Long ptMatchingId) {
//        // 로그인한 계정이 trainer인지 확인 (trainee는 PT를 수락할 권한 없음)
//        Account account = securityUtil.getLoginAccountEntity();
//        if (!account.getAccountRole().equals(Role.TRAINER)) {
//            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.AUTHENTICATED_USER_IS_NOT_TRAINER.getMessage());
//        }
//
//        // 해당 ptMatchingId가 존재하는지 확인
//        PtMatching ptMatching = ptMatchingRepository.findByIdAndIsDeletedFalse(ptMatchingId)
//                .orElseThrow(() -> new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.PT_MATCHING_NOT_FOUND.getMessage()));
//
//        // pt matching status가 pending인지 확인
//        if (!ptMatching.getStatus().equals(PtStatus.PENDING)) {
//            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.PT_MATCHING_STATUS_IS_NOT_PENDING.getMessage());
//        }
//
//        // pt matching의 trainerId가 로그인한 계정의 accountId와 일치하는지 확인
//        if (!ptMatching.getTrainee().getAccountId().equals(account.getAccountId())) {
//            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.PT_MATCHING_TRAINER_ID_IS_NOT_MATCHED.getMessage());
//        }
//
//        // pt matching의 status를 active로 변경
//        PtMatching saved = ptMatchingRepository.save(ptMatching.updateStatus(PtStatus.ACTIVE));
//        return ResultResponse.of(ResultCode.PT_MATCHING_ACCEPT_SUCCESS, toPtMatchingSummary.fromPtMatchingEntity(saved, account.getAccountId()));
        return null;
    }
}
