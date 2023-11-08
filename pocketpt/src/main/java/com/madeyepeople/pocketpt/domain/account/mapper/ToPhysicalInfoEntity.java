package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.request.PhysicalInfoCreateRequest;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.entity.PhysicalInfo;
import org.springframework.stereotype.Component;

@Component
public class ToPhysicalInfoEntity {
    public PhysicalInfo of(Account account, PhysicalInfoCreateRequest physicalInfoCreateRequest) {
        return PhysicalInfo.builder()
                .account(account)
                .date(physicalInfoCreateRequest.getDate())
                .weight(physicalInfoCreateRequest.getWeight())
                .skeletalMuscleMass(physicalInfoCreateRequest.getSkeletalMuscleMass())
                .bodyFatPercentage(physicalInfoCreateRequest.getBodyFatPercentage())
                .bmi(physicalInfoCreateRequest.getBmi())
                .waistHipRatio(physicalInfoCreateRequest.getWaistHipRatio())
                .inbodyScore(physicalInfoCreateRequest.getInbodyScore())
                .build();
    }
}
