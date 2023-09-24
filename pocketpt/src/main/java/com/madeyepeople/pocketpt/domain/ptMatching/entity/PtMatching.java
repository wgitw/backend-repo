package com.madeyepeople.pocketpt.domain.ptMatching.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatusEnumConverter;
import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatus;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "pt_matching")
@SQLDelete(sql = "UPDATE pt_matching SET is_deleted = true WHERE pt_matching_id = ?")
public class PtMatching extends BaseEntity {

    @Id
    @Column(name = "pt_matching_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ptMatchingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_account_id")
    @JsonIgnore
    private Account trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_account_id")
    @JsonIgnore
    private Account trainee;

    @Convert(converter = PtStatusEnumConverter.class)
    private PtStatus status;

    private Integer subscriptionPeriod;

    private Date startDate;

    private Date expiredDate;

    private Integer paymentAmount;

    private Boolean isNewSubscription;

    private String contactType;

    private String precaution;

    private String rejectReason;

    @Builder
    public PtMatching(Account trainer, Account trainee, Integer subscriptionPeriod, PtStatus status, Integer paymentAmount, Date startDate) {
        this.trainer = trainer;
        this.trainee = trainee;
        this.subscriptionPeriod = subscriptionPeriod;
        this.status = status;
        this.paymentAmount = paymentAmount;
        this.startDate = startDate;
    }

    public PtMatching updateStatusAndExpiredDate(PtStatus status, Date expiredDate) {
        this.status = status;
        this.expiredDate = expiredDate;
        return this;
    }

    public PtMatching updateRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
        return this;
    }

//    public PtMatching updateSales(Integer)
    public Account getOpponentAccountByMyAccountId(Long myAccountId) {
        if (this.trainer.getAccountId().equals(myAccountId)) {
            return this.trainee;
        } else if (this.trainee.getAccountId().equals(myAccountId)) {
            return this.trainer;
        } else {
            throw new IllegalArgumentException(CustomExceptionMessage.ACCOUNT_ID_NOT_EXIST_IN_PT_MATCHING.getMessage());
        }
    }

    // TODO: 매일 PtMatching table 조회해서 expiredDate가 오늘인 것들의 status = expired로 바꿔주는 로직 추가
}
