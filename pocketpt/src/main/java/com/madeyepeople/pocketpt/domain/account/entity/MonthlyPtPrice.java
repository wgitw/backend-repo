package com.madeyepeople.pocketpt.domain.account.entity;


import com.madeyepeople.pocketpt.domain.account.dto.request.TrainerMonthlyPtPriceCreateAndUpdateRequest;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyPtPrice extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monthly_pt_price_id")
    private Long monthlyPtPriceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account trainer;

    private Integer period;

    private Integer price;

    @Builder
    public MonthlyPtPrice(Account trainer, Integer period, Integer price) {
        this.trainer = trainer;
        this.period = period;
        this.price = price;
    }

    public void updateByTrainerMonthlyPtPriceCreateAndUpdateRequest(TrainerMonthlyPtPriceCreateAndUpdateRequest trainerMonthlyPtPriceCreateAndUpdateRequest) {
        if (trainerMonthlyPtPriceCreateAndUpdateRequest.getPeriod() != null) {
            this.period = trainerMonthlyPtPriceCreateAndUpdateRequest.getPeriod();
        }
        if (trainerMonthlyPtPriceCreateAndUpdateRequest.getPrice() != null) {
            this.price = trainerMonthlyPtPriceCreateAndUpdateRequest.getPrice();
        }
    }
}
