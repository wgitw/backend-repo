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
    public List<PtMatchingSummary> fromPtMatchingEntityList(List<PtMatching> ptMatchingList, Role requesterRole) {
        Role recieverRole = requesterRole == Role.TRAINER ? Role.TRAINEE : Role.TRAINER;
        List<PtMatchingSummary> ptMatchingSummaryList = ptMatchingList.stream()
                .map(ptMatching -> toPtMatchingSummary.fromPtMatchingEntity(ptMatching, recieverRole))
                .collect(Collectors.toList());

        return ptMatchingSummaryList;
    }
}
