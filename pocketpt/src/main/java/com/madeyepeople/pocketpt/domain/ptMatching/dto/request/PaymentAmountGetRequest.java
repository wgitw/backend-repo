package com.madeyepeople.pocketpt.domain.ptMatching.dto.request;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class PaymentAmountGetRequest {
    private Integer subscriptionPeriod;
    private List<MonthlyPtPriceDto> monthlyPtPriceList;
}
