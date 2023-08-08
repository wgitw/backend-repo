package com.madeyepeople.pocketpt.domain.ptMatching.mapper;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.PtMatchingSummary;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ToPtMatchingListResponse {

    private final ToPtMatchingSummary toPtMatchingSummary;
    public List<PtMatchingSummary> fromPtMatchingEntityList(List<PtMatching> ptMatchingList, Long requestorAccountId) {
        List<PtMatchingSummary> ptMatchingSummaryList = ptMatchingList.stream()
                // TODO: trainer도 pt를 신청할 수 있으므로, trainee는 Role이 아닌, AccountId로 비교해야함
                .map(ptMatching -> toPtMatchingSummary.fromPtMatchingEntity(ptMatching, requestorAccountId))
                .collect(Collectors.toList());

        return ptMatchingSummaryList;
    }
}
