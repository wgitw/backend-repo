package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.request.PurposeCreateRequest;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.entity.Purpose;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ToPurposeEntity {
    public Purpose of(Account account, PurposeCreateRequest purposeCreateRequest) {
        LocalDate targetDate;

        if (purposeCreateRequest.getTargetDate().isBlank()) {
            targetDate = null;
        } else {
            targetDate = LocalDate.parse(purposeCreateRequest.getTargetDate());
        }

        return Purpose.builder()
                .account(account)
                .title(purposeCreateRequest.getTitle())
                .content(purposeCreateRequest.getContent())
                .targetDate(targetDate)
                .build();
    }
}
