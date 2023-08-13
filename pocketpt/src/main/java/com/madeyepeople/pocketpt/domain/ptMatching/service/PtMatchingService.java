package com.madeyepeople.pocketpt.domain.ptMatching.service;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.mapper.ToMonthlyPtPriceDtoList;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.domain.chattingRoom.service.ChattingRoomService;
import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatus;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.PtMatchingSummary;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.request.PtRegistrationRequest;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.response.PtRegistrationAcceptResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.dto.response.PtRegistrationResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtMatchingEntity;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtMatchingListResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtMatchingSummary;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtRegistrationResponse;
import com.madeyepeople.pocketpt.domain.ptMatching.repository.PtMatchingRepository;
import com.madeyepeople.pocketpt.domain.ptMatching.util.PaymentAmountCalculator;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class PtMatchingService {
    private final ChattingRoomService chattingRoomService;

    private final AccountRepository accountRepository;
    private final PtMatchingRepository ptMatchingRepository;

    private final ToPtMatchingEntity toPtMatchingEntity;
    private final ToPtRegistrationResponse toPtRegistrationResponse;
    private final ToPtMatchingListResponse toPtMatchingListResponse;
    private final ToPtMatchingSummary toPtMatchingSummary;
    private final ToMonthlyPtPriceDtoList toMonthlyPtPriceDtoList;

    private final SecurityUtil securityUtil;
    private final PaymentAmountCalculator paymentAmountCalculator;

    private final SimpMessageSendingOperations template;

    @Transactional
    public PtRegistrationResponse registerPt(PtRegistrationRequest ptRegistrationRequest) throws ConstraintViolationException, BusinessException {
        // TODO:  중복 등록 불가하게 수정 (trainerId, traineeId, status = pending unique하게 변경)
        Optional<Account> trainer = accountRepository.findByIdentificationCodeAndIsDeletedFalse(ptRegistrationRequest.getTrainerCode());

        // 해당 Identification Code 가진 account가 있는지, 있다면 Role = trainer인지 확인
        if (trainer.isEmpty()) {
            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.TRAINER_IDENTIFICATION_CODE_NOT_FOUND.getMessage());
        } else if (!trainer.get().getAccountRole().getValue().equals("trainer")) {
            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.IDENTIFICATION_CODE_IS_NOT_TRAINER.getMessage());
        }

        Account trainee = securityUtil.getLoginAccountEntity();
        List<MonthlyPtPriceDto> monthlyPtPriceDtoList = toMonthlyPtPriceDtoList.of(trainer.get().getMonthlyPtPriceList());
        Integer paymentAmount = paymentAmountCalculator.calculate(ptRegistrationRequest.getSubscriptionPeriod(), monthlyPtPriceDtoList);

        PtMatching saved = ptMatchingRepository.save(toPtMatchingEntity.fromAccountEntities(trainer.get(), trainee, ptRegistrationRequest.getSubscriptionPeriod(), paymentAmount));

        // TODO: ResultResponse 사용하도록 변경, Account 로직들도 마찬가지
        return toPtRegistrationResponse.fromPtMatchingEntity(saved);
    }

    @Transactional
    public ResultResponse getPtMatchingList(String mode) {
        Account account = securityUtil.getLoginAccountEntity();
        List<PtMatching> ptMatchingList;

        if (account.getAccountRole().equals(Role.TRAINER)) {
            if (mode.equals("all")) {
                ptMatchingList = ptMatchingRepository.findAllByTrainerAccountIdAndIsDeletedFalseOrderByCreatedAtDesc(account.getAccountId());
            } else {
                ptMatchingList = ptMatchingRepository.findAllByTrainerAccountIdAndIsDeletedFalseAndStatusOrderByCreatedAtDesc(account.getAccountId(), PtStatus.valueOf(mode.toUpperCase()));
            }
        } else {
            if (mode.equals("all")) {
                ptMatchingList = ptMatchingRepository.findAllByTraineeAccountIdAndIsDeletedFalseOrderByCreatedAtDesc(account.getAccountId());
            } else {
                ptMatchingList = ptMatchingRepository.findAllByTraineeAccountIdAndIsDeletedFalseAndStatusOrderByCreatedAtDesc(account.getAccountId(), PtStatus.valueOf(mode.toUpperCase()));
            }
        }

        // 내 accountId를 이용해 PtMatching 정보들 중, 상대방 정보만 추출
        List<PtMatchingSummary> ptMatchingSummaryList = toPtMatchingListResponse.fromPtMatchingEntityList(ptMatchingList, account.getAccountId());

        return ResultResponse.of(ResultCode.PT_MATCHING_LIST_GET_SUCCESS, ptMatchingSummaryList);
    }

    @Transactional
    public ResultResponse acceptPtMatching(Long ptMatchingId) {

        // 로그인한 계정이 trainer인지 확인 (trainee는 PT를 수락할 권한 없음)
        Account trainer = securityUtil.getLoginAccountEntity();
        if (!trainer.getAccountRole().equals(Role.TRAINER)) {
            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.AUTHENTICATED_USER_IS_NOT_TRAINER.getMessage());
        }

        // 해당 ptMatchingId가 존재하는지 확인
        PtMatching ptMatching = ptMatchingRepository.findByPtMatchingIdAndIsDeletedFalse(ptMatchingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.PT_MATCHING_NOT_FOUND.getMessage()));

        // pt matching의 trainerId가 로그인한 계정의 accountId와 일치하는지 확인
        if (!ptMatching.getTrainer().getAccountId().equals(trainer.getAccountId())) {
            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.PT_MATCHING_TRAINER_ID_IS_NOT_MATCHED.getMessage());
        }

        // pt matching status가 pending인지 확인
        if (!ptMatching.getStatus().equals(PtStatus.PENDING)) {
            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.PT_MATCHING_STATUS_IS_NOT_PENDING.getMessage());
        }

        Integer updatedTotalSales = trainer.getTotalSales() + ptMatching.getPaymentAmount();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, ptMatching.getSubscriptionPeriod());
        Date updatedExpiredDate = calendar.getTime();

        PtMatching savedPtMatching = ptMatchingRepository.save(ptMatching.updateStatusAndExpiredDate(PtStatus.ACTIVE, updatedExpiredDate));
        Account savedTrainer = accountRepository.save(trainer.updateTotalSales(updatedTotalSales));

        // 채팅방 생성 및 생성 응답 전송
        ResultResponse resultResponse = chattingRoomService.createChattingRoomFromPtMatching(ptMatching.getTrainer(), ptMatching.getTrainee());
        template.convertAndSend("/sub/accounts/" + ptMatching.getTrainer().getAccountId(), resultResponse);
        template.convertAndSend("/sub/accounts/" + ptMatching.getTrainee().getAccountId(), resultResponse);

        PtRegistrationAcceptResponse ptRegistrationAcceptResponse = PtRegistrationAcceptResponse.builder()
                .ptMatchingSummary(toPtMatchingSummary.fromPtMatchingEntity(savedPtMatching, trainer.getAccountId()))
                .totalSales(savedTrainer.getTotalSales())
                .build();

        return ResultResponse.of(ResultCode.PT_MATCHING_ACCEPT_SUCCESS, ptRegistrationAcceptResponse);
    }
}
