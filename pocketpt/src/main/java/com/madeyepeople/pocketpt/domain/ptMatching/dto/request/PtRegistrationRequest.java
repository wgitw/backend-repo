package com.madeyepeople.pocketpt.domain.ptMatching.dto.request;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PtRegistrationRequest {
    private Long trainerAccountId;
    private Integer subscriptionPeriod;
    private Integer paymentAmount;
}
