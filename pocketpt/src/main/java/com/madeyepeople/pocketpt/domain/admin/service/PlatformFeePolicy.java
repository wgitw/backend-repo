package com.madeyepeople.pocketpt.domain.admin.service;

public interface PlatformFeePolicy {

    /*
    * @return 정책에 따른 순수익 계산
     */
    Integer calculateIncome(Integer sales);
}
