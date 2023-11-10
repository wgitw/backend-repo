package com.madeyepeople.pocketpt.domain.account.entity;

import com.madeyepeople.pocketpt.domain.account.dto.request.PhysicalInfoCreateRequest;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "physical_info",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "physical_info_uk",
                        columnNames = {"account_id", "date"}
                )
        }
)
public class PhysicalInfo extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "physical_info_id")
    private Long physicalInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private LocalDate date;

    private Float weight;

    private Float skeletalMuscleMass;

    private Float bodyFatPercentage;

    private Float bmi;

    private Float waistHipRatio;

    private Integer inbodyScore;

    @Builder
    public PhysicalInfo(Account account, LocalDate date, Float weight, Float skeletalMuscleMass, Float bodyFatPercentage, Float bmi, Float waistHipRatio, Integer inbodyScore) {
        this.account = account;
        this.date = date;
        this.weight = weight;
        this.skeletalMuscleMass = skeletalMuscleMass;
        this.bodyFatPercentage = bodyFatPercentage;
        this.bmi = bmi;
        this.waistHipRatio = waistHipRatio;
        this.inbodyScore = inbodyScore;
    }
}
