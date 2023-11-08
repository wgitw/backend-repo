package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.PhysicalInfoDto;
import com.madeyepeople.pocketpt.domain.account.entity.PhysicalInfo;
import org.springframework.stereotype.Component;

@Component
public class ToPhysicalInfoDto {
    public PhysicalInfoDto of(PhysicalInfo physicalInfo) {
        return PhysicalInfoDto.builder()
                .accountId(physicalInfo.getAccount().getAccountId())
                .physicalInfoId(physicalInfo.getPhysicalInfoId())
                .date(physicalInfo.getDate())
                .weight(physicalInfo.getWeight())
                .skeletalMuscleMass(physicalInfo.getSkeletalMuscleMass())
                .bodyFatPercentage(physicalInfo.getBodyFatPercentage())
                .bmi(physicalInfo.getBmi())
                .waistHipRatio(physicalInfo.getWaistHipRatio())
                .inbodyScore(physicalInfo.getInbodyScore())
                .build();
    }
}
