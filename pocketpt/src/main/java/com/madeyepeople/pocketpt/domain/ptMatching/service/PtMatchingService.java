package com.madeyepeople.pocketpt.domain.ptMatching.service;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.request.PtRegistrationRequest;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.response.PtRegistrationResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtMatchingEntity;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtRegistrationResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.repository.PtMatchingRepository;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class PtMatchingService {

    private final AccountRepository accountRepository;
    private final PtMatchingRepository ptMatchingRepository;
    private final ToPtMatchingEntity toPtMatchingEntity;
    private final ToPtRegistrationResponse toPtRegistrationResponse;
    private final SecurityUtil securityUtil;

    public PtRegistrationResponse registerPt(PtRegistrationRequest ptRegistrationRequest) throws ConstraintViolationException, BusinessException {
        Optional<Account> trainer = accountRepository.findByIdentificationCodeAndIsDeletedFalse(ptRegistrationRequest.getTrainerCode());
        if (trainer.isEmpty()) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, CustomExceptionMessage.TRAINER_IDENTIFICATION_CODE_NOT_FOUND.getMessage());
        }

        Account trainee = securityUtil.getLoginAccountEntity();

        PtMatching saved = ptMatchingRepository.save(toPtMatchingEntity.fromAccountEntities(trainer.get(), trainee));
        return toPtRegistrationResponse.fromPtMatchingEntity(saved);
    }
}
