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

    private final SecurityUtil securityUtil;

    public PtRegistrationResponse registerPt(PtRegistrationRequest ptRegistrationRequest) throws ConstraintViolationException, BusinessException {
        // TODO:  중복 등록 불가하게 수정 (trainerId, traineeId, status = pending unique하게 변경)
        Optional<Account> trainer = accountRepository.findByIdentificationCodeAndIsDeletedFalse(ptRegistrationRequest.getTrainerCode());
        if (trainer.isEmpty()) {
            throw new BusinessException(ErrorCode.TRAINER_IDENTIFICATION_CODE_NOT_FOUND, CustomExceptionMessage.TRAINER_IDENTIFICATION_CODE_NOT_FOUND.getMessage());
        } else if (!trainer.get().getAccountRole().getValue().equals("trainer")) {
            throw new BusinessException(ErrorCode.TRAINER_IDENTIFICATION_CODE_NOT_FOUND, CustomExceptionMessage.IDENTIFICATION_CODE_IS_NOT_TRAINER.getMessage());
        }

        Account trainee = securityUtil.getLoginAccountEntity();

        // TODO: subscriptionPeriod로 expiredDate도 계산해야 함
        PtMatching saved = ptMatchingRepository.save(toPtMatchingEntity.fromAccountEntities(trainer.get(), trainee, ptRegistrationRequest.getSubscriptionPeriod()));
        // TODO: ResultResponse 사용하도록 변경, Account 로직들도 마찬가지
        return toPtRegistrationResponse.fromPtMatchingEntity(saved);
    }

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

        List<PtMatchingSummary> ptMatchingSummaryList = toPtMatchingListResponse.fromPtMatchingEntityList(ptMatchingList, account.getAccountRole());

        return ResultResponse.of(ResultCode.PT_MATCHING_LIST_GET_SUCCESS, ptMatchingSummaryList);
    }
}
