package com.madeyepeople.pocketpt.domain.ptMatching.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class PtMatchingSummary {
    // ptMatching info
    private Long ptMatchingId;
    private Long chattingRoomId;
    private String status;
    private Integer subscriptionPeriod;
    private Integer paymentAmount;
    private String startDate;
    private String expiredDate;
    private String rejectReason;
    private String trainerMemo;

    // account info
    private Long accountId;
    private String name;
    private String phoneNumber;
    private String email;
    private String profilePictureUrl;
}
