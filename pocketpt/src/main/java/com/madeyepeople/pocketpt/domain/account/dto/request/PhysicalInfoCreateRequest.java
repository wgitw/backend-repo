package com.madeyepeople.pocketpt.domain.account.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class PhysicalInfoCreateRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;
    private Float weight;
    private Float skeletalMuscleMass;
    private Float bodyFatPercentage;
    private Float bmi;
    private Float waistHipRatio;
    private Integer inbodyScore;
}
