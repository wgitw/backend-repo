package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.dto.response.AccountRegistrationResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.entity.MonthlyPtPrice;
import com.madeyepeople.pocketpt.domain.account.util.TrainerMonthlyPtPriceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ToRegistrationResponse {

    private final TrainerMonthlyPtPriceUtil trainerMonthlyPtPriceUtil;

    public AccountRegistrationResponse fromAccountEntity(Account account) {
        List<MonthlyPtPriceDto> monthlyPtPriceDtoList = account.getMonthlyPtPriceList().stream()
                .map(monthlyPtPrice -> MonthlyPtPriceDto.builder()
                        .monthlyPtPriceId(monthlyPtPrice.getMonthlyPtPriceId())
                        .period(monthlyPtPrice.getPeriod())
                        .price(monthlyPtPrice.getPrice())
                        .build())
                .collect(Collectors.toList()); // 정렬을 위해 immutable한 Collectors.toList() 사용

        return AccountRegistrationResponse.builder()
                .accountId(account.getAccountId())
                .role(account.getAccountRole())
                .identificationCode(account.getIdentificationCode())
                .email(account.getEmail())
                .name(account.getName())
                .phoneNumber(account.getPhoneNumber())
                .nickname(account.getNickname())
                .monthlyPtPriceList(trainerMonthlyPtPriceUtil.sortByPeriod(monthlyPtPriceDtoList))
                .build();
    }
}
