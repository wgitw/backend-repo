package com.madeyepeople.pocketpt.domain.ptMatching.mapper;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.PtMatchingSummary;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import org.springframework.stereotype.Component;

@Component
public class ToPtMatchingSummary {
    public PtMatchingSummary fromPtMatchingEntity(PtMatching ptMatching, Role recieverRole) {
        return PtMatchingSummary.builder()
                .ptMatchingId(ptMatching.getPtMatchingId())
                .status(ptMatching.getStatus().getValue())
                .subscriptionPeriod(ptMatching.getSubscriptionPeriod())
                .expiredDate(ptMatching.getExpiredDate())
                .name(ptMatching.getAccountByRole(recieverRole).getName())
                .phoneNumber(ptMatching.getAccountByRole(recieverRole).getPhoneNumber())
                .email(ptMatching.getAccountByRole(recieverRole).getEmail())
                .profilePictureUrl(ptMatching.getAccountByRole(recieverRole).getProfilePictureUrl())
                .build();
    }
}
