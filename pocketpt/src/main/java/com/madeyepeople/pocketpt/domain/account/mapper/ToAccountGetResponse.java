package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.dto.PurposeDto;
import com.madeyepeople.pocketpt.domain.account.dto.response.AccountDetailGetResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ToAccountGetResponse {

    private final ToTrainerCareerCreateAndGetResponse toTrainerCareerCreateAndGetResponse;
    private final ToMonthlyPtPriceDtoList toMonthlyPtPriceDtoList;
    private final ToPurposeDto toPurposeDto;

    public AccountDetailGetResponse fromAccountEntity(Account account) {
        List<CareerDto> careerDtoList = toTrainerCareerCreateAndGetResponse.of(account.getCareerList()).getCareerList();
        List<MonthlyPtPriceDto> monthlyPtPriceDtoList = toMonthlyPtPriceDtoList.of(account.getMonthlyPtPriceList());
        List<PurposeDto> purposeDtoList = toPurposeDto.of(account.getPurposeList());

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
                .careerList(careerDtoList)
                .monthlyPtPriceList(monthlyPtPriceDtoList)
                .purposeList(purposeDtoList)
                .build();
    }
}
