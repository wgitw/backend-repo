package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.request.TrainerMonthlyPtPriceCreateAndUpdateRequest;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.entity.MonthlyPtPrice;
import org.springframework.stereotype.Component;

@Component
public class ToMonthlyPtPriceEntity {
    public MonthlyPtPrice of(Account trainer, TrainerMonthlyPtPriceCreateAndUpdateRequest trainerMonthlyPtPriceCreateAndUpdateRequest) {
        return MonthlyPtPrice.builder()
                .trainer(trainer)
                .period(trainerMonthlyPtPriceCreateAndUpdateRequest.getPeriod())
                .price(trainerMonthlyPtPriceCreateAndUpdateRequest.getPrice())
                .build();
    }
}
