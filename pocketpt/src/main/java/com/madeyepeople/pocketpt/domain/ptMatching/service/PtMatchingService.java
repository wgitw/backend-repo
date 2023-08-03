package com.madeyepeople.pocketpt.domain.ptMatching.service;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.request.PtRegistrationRequest;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.response.PtRegistrationResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.repository.PtMatchingRepository;
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
    private final SecurityUtil securityUtil;

    public PtRegistrationResponse registerPt(PtRegistrationRequest ptRegistrationRequest) throws ConstraintViolationException, BusinessException {
        // TODO: trainerCode 검증
        Optional<Account> trainer = accountRepository.findByIdentificationCodeAndIsDeletedFalse(ptRegistrationRequest.getTrainerCode());
        if (trainer.isEmpty()) {
            throw new BusinessException(CustomExceptionMessage.TRAINER_IDENTIFICATION_CODE_NOT_FOUND.getMessage(), null);
        }

        // TODO: ptMatching entity 생성

        return null;
    }
}
