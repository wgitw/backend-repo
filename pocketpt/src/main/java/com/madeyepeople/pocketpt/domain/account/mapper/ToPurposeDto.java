package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.PurposeDto;
import com.madeyepeople.pocketpt.domain.account.entity.Purpose;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ToPurposeDto {
    public PurposeDto of(Purpose purpose) {
        String targetDate = purpose.getTargetDate() == null ? null : purpose.getTargetDate().toString();

        return PurposeDto.builder()
                .purposeId(purpose.getPurposeId())
                .title(purpose.getTitle())
                .content(purpose.getContent())
                .targetDate(targetDate)
                .build();
    }

    public List<PurposeDto> of(List<Purpose> purposeList) {
        return purposeList.stream()
                .map(this::of)
                .collect(Collectors.toList());
    }
}
