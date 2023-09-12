package com.madeyepeople.pocketpt.domain.ptMatching.mapper;

import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatus;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.response.PtRegistrationResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component
public class ToPtRegistrationResponse {
    public PtRegistrationResponse fromPtMatchingEntity(PtMatching ptMatching) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate;
        startDate = dateFormat.format(ptMatching.getStartDate());

        return PtRegistrationResponse.builder()
                .ptMatchingId(ptMatching.getPtMatchingId())
                .trainerId(ptMatching.getTrainer().getAccountId())
                .trainerName(ptMatching.getTrainer().getName())
                .traineeId(ptMatching.getTrainee().getAccountId())
                .traineeName(ptMatching.getTrainee().getName())
                .startDate(startDate)
                .subscriptionPeriod(ptMatching.getSubscriptionPeriod())
                .paymentAmount(ptMatching.getPaymentAmount())
                .status(ptMatching.getStatus())
                .build();
    }
}
