package com.madeyepeople.pocketpt.domain.ptMatching.mapper;

import com.madeyepeople.pocketpt.domain.ptMatching.dto.response.PtRegistrationResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import org.springframework.stereotype.Component;

@Component
public class ToPtRegistrationResponse {
    public PtRegistrationResponse fromPtMatchingEntity(PtMatching ptMatching) {
        return PtRegistrationResponse.builder()
                .ptMatchingId(ptMatching.getPtMatchingId())
                .trainerId(ptMatching.getTrainer().getAccountId())
                .traineeId(ptMatching.getTrainee().getAccountId())
                .status(ptMatching.getStatus())
                .build();
    }
}
