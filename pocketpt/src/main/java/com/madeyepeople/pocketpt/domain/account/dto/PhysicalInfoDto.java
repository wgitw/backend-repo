package com.madeyepeople.pocketpt.domain.account.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class PhysicalInfoDto {

    private Long physicalInfoId;

    private Long accountId;

    private LocalDate date;

    private Float weight;

    private Float skeletalMuscleMass;

    private Float bodyFatPercentage;

    private Float bmi;

    private Float waistHipRatio;

    private Integer inbodyScore;
}
