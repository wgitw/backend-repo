package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.PhysicalInfoDto;
import com.madeyepeople.pocketpt.domain.account.entity.PhysicalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ToPhysicalInfoDtoList {
    private final ToPhysicalInfoDto toPhysicalInfoDto;

    public List<PhysicalInfoDto> of(List<PhysicalInfo> physicalInfoList) {
        return physicalInfoList.stream()
                .map(toPhysicalInfoDto::of)
                .collect(Collectors.toList());
    }
}
