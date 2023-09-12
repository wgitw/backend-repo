package com.madeyepeople.pocketpt.domain.ptMatching.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PtMatchingSummary {
    // ptMatching info
    private Long ptMatchingId;
    private String status;
    private Integer subscriptionPeriod;
    private Integer paymentAmount;
    private String startDate;
    private String expiredDate;

    // account info
    private Long accountId;
    private String name;
    private String phoneNumber;
    private String email;
    private String profilePictureUrl;
}
