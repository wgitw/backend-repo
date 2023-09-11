package com.madeyepeople.pocketpt.domain.admin.service;

import com.madeyepeople.pocketpt.domain.admin.repository.PlatformFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FixedPlatformFeePolicy implements PlatformFeePolicy {

    private final PlatformFeeRepository platformFeeRepository;

    @Override
    public Integer calculateIncome(Integer sales) {
        // TODO: month 수도 받아서 month 수만큼 곱한 값 return하게 수정
        return sales - platformFeeRepository.getFixedPlatformFeeAmount();
    }
}
