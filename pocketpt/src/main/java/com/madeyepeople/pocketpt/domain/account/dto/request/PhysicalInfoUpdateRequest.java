package com.madeyepeople.pocketpt.domain.account.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PhysicalInfoUpdateRequest {
    private Float weight;
    private Float skeletalMuscleMass;
    private Float bodyFatPercentage;
    private Float bmi;
    private Float waistHipRatio;
    private Integer inbodyScore;
}
