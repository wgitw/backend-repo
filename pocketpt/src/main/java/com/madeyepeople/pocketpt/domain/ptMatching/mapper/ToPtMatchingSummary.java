package com.madeyepeople.pocketpt.domain.ptMatching.mapper;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatus;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.PtMatchingSummary;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ToPtMatchingSummary {

    public PtMatchingSummary fromPtMatchingEntity(PtMatching ptMatching, Long MyAccountId) {
        Account opponent = ptMatching.getOpponentAccountByMyAccountId(MyAccountId);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate, expiredDate;
        if (ptMatching.getStatus() == PtStatus.PENDING) {
            startDate = null;
            expiredDate = null;
        } else {
            startDate = dateFormat.format(ptMatching.getStartDate());
            expiredDate = dateFormat.format(ptMatching.getExpiredDate());
        }

        return PtMatchingSummary.builder()
                .ptMatchingId(ptMatching.getPtMatchingId())
                .status(ptMatching.getStatus().getValue())
                .subscriptionPeriod(ptMatching.getSubscriptionPeriod())
                .paymentAmount(ptMatching.getPaymentAmount())
                .startDate(startDate)
                .expiredDate(expiredDate)
                .rejectReason(ptMatching.getRejectReason())
                .accountId(opponent.getAccountId())
                .name(opponent.getName())
                .phoneNumber(opponent.getPhoneNumber())
                .email(opponent.getEmail())
                .profilePictureUrl(opponent.getProfilePictureUrl())
                .build();
    }
}
