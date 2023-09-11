package com.madeyepeople.pocketpt.domain.admin.service;

import com.madeyepeople.pocketpt.domain.admin.repository.PlatformFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RelativePlatformFeePolicy implements PlatformFeePolicy {

    private final PlatformFeeRepository platformFeeRepository;

    @Override
    public Integer calculateIncome(Integer sales) {
        return (int) (sales * (1 - platformFeeRepository.getRelativePlatformFeeAmount()));
    }
}
