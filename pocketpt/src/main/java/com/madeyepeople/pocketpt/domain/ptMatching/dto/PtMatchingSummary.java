package com.madeyepeople.pocketpt.domain.ptMatching.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class PtMatchingSummary {
    // ptMatching info
    private Long ptMatchingId;
    private String status;
    private Integer subscriptionPeriod;
    private Integer paymentAmount;
    private Date expiredDate;

    // account info
    private Long accountId;
    private String name;
    private String phoneNumber;
    private String email;
    private String profilePictureUrl;
}
