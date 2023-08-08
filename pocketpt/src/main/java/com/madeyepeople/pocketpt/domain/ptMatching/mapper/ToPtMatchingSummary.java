package com.madeyepeople.pocketpt.domain.ptMatching.mapper;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.PtMatchingSummary;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import org.springframework.stereotype.Component;

@Component
public class ToPtMatchingSummary {
//    public PtMatchingSummary fromPtMatchingEntity(PtMatching ptMatching, Role traineeRole) {
//        return PtMatchingSummary.builder()
//                .ptMatchingId(ptMatching.getPtMatchingId())
//                .status(ptMatching.getStatus().getValue())
//                .subscriptionPeriod(ptMatching.getSubscriptionPeriod())
//                .expiredDate(ptMatching.getExpiredDate())
//                .name(ptMatching.getAccountByRole(traineeRole).getName())
//                .phoneNumber(ptMatching.getAccountByRole(traineeRole).getPhoneNumber())
//                .email(ptMatching.getAccountByRole(traineeRole).getEmail())
//                .profilePictureUrl(ptMatching.getAccountByRole(traineeRole).getProfilePictureUrl())
//                .build();
//    }

    public PtMatchingSummary fromPtMatchingEntity(PtMatching ptMatching, Long MyAccountId) {
        Account opponent = ptMatching.getOpponentAccountByMyAccountId(MyAccountId);
        return PtMatchingSummary.builder()
                .ptMatchingId(ptMatching.getPtMatchingId())
                .status(ptMatching.getStatus().getValue())
                .subscriptionPeriod(ptMatching.getSubscriptionPeriod())
                .expiredDate(ptMatching.getExpiredDate())
                .name(opponent.getName())
                .phoneNumber(opponent.getPhoneNumber())
                .email(opponent.getEmail())
                .profilePictureUrl(opponent.getProfilePictureUrl())
                .build();
    }
}
