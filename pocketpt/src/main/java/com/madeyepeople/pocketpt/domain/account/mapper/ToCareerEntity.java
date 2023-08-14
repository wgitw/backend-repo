package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.constant.CareerType;
import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.entity.Career;
import org.springframework.stereotype.Component;

@Component
public class ToCareerEntity {
    public Career of(Account trainer, CareerDto careerDto) {
        return Career.builder()
                .trainer(trainer)
                .type(CareerType.valueOf(careerDto.getType().toUpperCase()))
                .title(careerDto.getTitle())
                .date(careerDto.getDate())
                .build();
    }
}
