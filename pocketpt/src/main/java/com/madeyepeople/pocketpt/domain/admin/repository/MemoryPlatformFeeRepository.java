package com.madeyepeople.pocketpt.domain.admin.repository;

import org.springframework.stereotype.Repository;

@Repository
public class MemoryPlatformFeeRepository implements PlatformFeeRepository {

    private final Integer fixedPlatformFeeAmount = 50000;
    private final Float relativePlatformFeeAmount = 0.05f;

    @Override
    public Integer getFixedPlatformFeeAmount() {
        return fixedPlatformFeeAmount;
    }

    @Override
    public Float getRelativePlatformFeeAmount() {
        return relativePlatformFeeAmount;
    }
}
