package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import com.madeyepeople.pocketpt.domain.account.entity.Career;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ToCareerDto {
    public List<CareerDto> of(List<Career> careerList) {
        return careerList.stream()
                .map(career -> CareerDto.builder()
                        .careerId(career.getCareerId())
                        .type(career.getType().getValue())
                        .title(career.getTitle())
                        .date(career.getDate())
                        .build())
                .collect(Collectors.toList());
    }
}
