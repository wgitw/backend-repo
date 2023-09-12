package com.madeyepeople.pocketpt.domain.ptMatching.mapper;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatus;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ToPtMatchingEntity {
    public PtMatching fromAccountEntities(Account trainer, Account trainee, Integer subscriptionPeriod, Integer paymentAmount, Date startDate) {
        return PtMatching.builder()
                .trainer(trainer)
                .trainee(trainee)
                .startDate(startDate)
                .subscriptionPeriod(subscriptionPeriod)
                .paymentAmount(paymentAmount)
                .status(PtStatus.PENDING)
                .build();
    }

}
