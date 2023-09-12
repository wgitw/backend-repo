package com.madeyepeople.pocketpt.domain.ptMatching.dto.response;

import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PtRegistrationResponse {
    private Long ptMatchingId;
    private Long trainerId;
    private String trainerName;
    private Long traineeId;
    private String traineeName;
    private String startDate;
    private Integer subscriptionPeriod;
    private Integer paymentAmount;
    private PtStatus status;
}
