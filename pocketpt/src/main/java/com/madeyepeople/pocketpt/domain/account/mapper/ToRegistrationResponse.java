package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.dto.response.AccountRegistrationResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.entity.MonthlyPtPrice;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ToRegistrationResponse {
    public AccountRegistrationResponse fromAccountEntity(Account account) {
        return AccountRegistrationResponse.builder()
                .accountId(account.getAccountId())
                .role(account.getAccountRole())
                .email(account.getEmail())
                .name(account.getName())
                .phoneNumber(account.getPhoneNumber())
                .nickname(account.getNickname())
                .monthlyPtPriceDtoList(
                        account.getMonthlyPtPriceList().stream()
                                .map(monthlyPtPrice -> MonthlyPtPriceDto.builder()
                                        .monthlyPtPriceId(monthlyPtPrice.getMonthlyPtPriceId())
                                        .period(monthlyPtPrice.getPeriod())
                                        .price(monthlyPtPrice.getPrice())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }
}
