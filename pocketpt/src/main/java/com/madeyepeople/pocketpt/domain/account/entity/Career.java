package com.madeyepeople.pocketpt.domain.account.entity;

import com.madeyepeople.pocketpt.domain.account.constant.CareerType;
import com.madeyepeople.pocketpt.domain.account.dto.CareerUpdateDto;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Career extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id")
    private Long careerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account trainer;

    private CareerType type;

    private String title;

    private String date;

    @Builder
    public Career(Account trainer, CareerType type, String title, String date) {
        this.trainer = trainer;
        this.type = type;
        this.title = title;
        this.date = date;
    }

    public Career updateByCareerUpdateDto(CareerUpdateDto careerUpdateDto) {
        if (careerUpdateDto.getTitle() != null) {
            this.title = careerUpdateDto.getTitle();
        }
        if (careerUpdateDto.getDate() != null) {
            this.date = careerUpdateDto.getDate();
        }
        return this;
    }
}
