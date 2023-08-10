package com.madeyepeople.pocketpt.domain.account.entity;


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
    private Account account;

    private Integer period;

    private Integer price;

    @Builder
    public MonthlyPtPrice(Account account, Integer period, Integer price) {
        this.account = account;
        this.period = period;
        this.price = price;
    }
}
