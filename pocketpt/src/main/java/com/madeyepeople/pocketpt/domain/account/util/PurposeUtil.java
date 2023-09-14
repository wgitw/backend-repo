package com.madeyepeople.pocketpt.domain.account.util;

import com.madeyepeople.pocketpt.domain.account.dto.PurposeDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurposeUtil {
    public List<PurposeDto> sortByPurposeId(List<PurposeDto> purposeDtoList) {
        return purposeDtoList.stream()
                .sorted(Comparator.comparing(PurposeDto::getPurposeId))
                .collect(Collectors.toList());
    }
}
