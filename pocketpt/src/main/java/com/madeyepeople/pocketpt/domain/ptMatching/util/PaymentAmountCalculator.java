package com.madeyepeople.pocketpt.domain.ptMatching.util;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentAmountCalculator {
    public Integer calculate(Integer subscriptionPeriod, List<MonthlyPtPriceDto> monthlyPtPrices) {
        int targetIndex = 0;
        for (MonthlyPtPriceDto monthlyPtPrice : monthlyPtPrices) {
            if (monthlyPtPrice.getPeriod() > subscriptionPeriod) {
                break;
            }
            targetIndex += 1;
        }

        int targetPrice = monthlyPtPrices.get(targetIndex - 1).getPrice();
        int targetMonth = monthlyPtPrices.get(targetIndex - 1).getPeriod();

        return ((int)(targetPrice / targetMonth)) * subscriptionPeriod;
    }
}
