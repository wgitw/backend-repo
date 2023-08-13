package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.dto.response.AccountDetailGetResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ToAccountGetResponse {
    public AccountDetailGetResponse fromAccountEntity(Account account) {

        return AccountDetailGetResponse.builder()
                .accountId(account.getAccountId())
                .role(account.getAccountRole())
                .provider(account.getProvider())
                .email(account.getEmail())
                .name(account.getName())
                .phoneNumber(account.getPhoneNumber())
                .nickname(account.getNickname())
                .profilePictureUrl(account.getProfilePictureUrl())
                .gender(account.getGender())
                .birthdate(account.getBirthdate())
                .introduce(account.getIntroduce())
                .identificationCode(account.getIdentificationCode())
                .totalSales(account.getTotalSales())
                .monthlyPtPriceList(
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
