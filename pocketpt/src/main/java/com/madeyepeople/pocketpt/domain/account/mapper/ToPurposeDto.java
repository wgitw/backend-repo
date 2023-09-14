package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.PurposeDto;
import com.madeyepeople.pocketpt.domain.account.entity.Purpose;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ToPurposeDto {
    public PurposeDto of(Purpose purpose) {
        String targetDate = purpose.getTargetDate() == null ? null : purpose.getTargetDate().toString();
        Integer dDay = purpose.getTargetDate() == null ? null : getDday(purpose);

        return PurposeDto.builder()
                .purposeId(purpose.getPurposeId())
                .title(purpose.getTitle())
                .content(purpose.getContent())
                .targetDate(targetDate)
                .dDay(dDay)
                .build();
    }

    public List<PurposeDto> of(List<Purpose> purposeList) {
        return purposeList.stream()
                .map(this::of)
                .collect(Collectors.toList());
    }

    public Integer getDday(Purpose purpose) {
        LocalDate today = LocalDate.now();
        LocalDate targetDate = purpose.getTargetDate();

        return Math.toIntExact(ChronoUnit.DAYS.between(today, targetDate));
    }
}
