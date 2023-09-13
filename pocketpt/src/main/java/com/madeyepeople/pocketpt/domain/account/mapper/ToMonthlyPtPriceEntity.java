package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.request.TrainerMonthlyPtPriceCreateRequest;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.entity.MonthlyPtPrice;
import org.springframework.stereotype.Component;

@Component
public class ToMonthlyPtPriceEntity {
    public MonthlyPtPrice of(Account trainer, TrainerMonthlyPtPriceCreateRequest trainerMonthlyPtPriceCreateRequest) {
        return MonthlyPtPrice.builder()
                .trainer(trainer)
                .period(trainerMonthlyPtPriceCreateRequest.getPeriod())
                .price(trainerMonthlyPtPriceCreateRequest.getPrice())
                .build();
    }
}
