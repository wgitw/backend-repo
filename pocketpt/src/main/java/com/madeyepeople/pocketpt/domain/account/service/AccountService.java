package com.madeyepeople.pocketpt.domain.account.service;

import com.madeyepeople.pocketpt.domain.account.dto.request.TrainerRegistrationRequest;
import com.madeyepeople.pocketpt.domain.account.dto.response.RegisterTrainerResponse;
import com.madeyepeople.pocketpt.domain.account.mapper.ToAccountEntity;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.global.s3.S3FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final ToAccountEntity toAccountEntity;
    private final S3FileService s3FileService;
    public RegisterTrainerResponse registerTrainer(TrainerRegistrationRequest trainerRegistrationRequest) {
        String fileUrl = s3FileService.uploadFile("account/career-certificate/", trainerRegistrationRequest.getCareerCertificate());
        return null;
    }
}
