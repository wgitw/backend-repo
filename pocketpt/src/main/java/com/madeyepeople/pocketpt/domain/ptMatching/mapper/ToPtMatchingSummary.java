package com.madeyepeople.pocketpt.domain.ptMatching.mapper;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.PtMatchingSummary;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import org.springframework.stereotype.Component;

@Component
public class ToPtMatchingSummary {

    public PtMatchingSummary fromPtMatchingEntity(PtMatching ptMatching, Long MyAccountId) {
        Account opponent = ptMatching.getOpponentAccountByMyAccountId(MyAccountId);
        return PtMatchingSummary.builder()
                .ptMatchingId(ptMatching.getPtMatchingId())
                .status(ptMatching.getStatus().getValue())
                .subscriptionPeriod(ptMatching.getSubscriptionPeriod())
                .paymentAmount(ptMatching.getPaymentAmount())
                .expiredDate(ptMatching.getExpiredDate())
                .accountId(opponent.getAccountId())
                .name(opponent.getName())
                .phoneNumber(opponent.getPhoneNumber())
                .email(opponent.getEmail())
                .profilePictureUrl(opponent.getProfilePictureUrl())
                .build();
    }
}
