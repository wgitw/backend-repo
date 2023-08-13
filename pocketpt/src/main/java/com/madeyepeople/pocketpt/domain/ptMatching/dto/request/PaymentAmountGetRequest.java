package com.madeyepeople.pocketpt.domain.ptMatching.dto.request;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PaymentAmountGetRequest {
    private Integer subscriptionPeriod;
    private List<MonthlyPtPriceDto> monthlyPtPriceList;
}
