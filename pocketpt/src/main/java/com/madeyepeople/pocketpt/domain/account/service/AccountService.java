package com.madeyepeople.pocketpt.domain.account.service;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import com.madeyepeople.pocketpt.domain.account.dto.CareerUpdateDto;
import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.dto.request.CommonRegistrationRequest;
import com.madeyepeople.pocketpt.domain.account.dto.request.TrainerCareerCreateRequest;
import com.madeyepeople.pocketpt.domain.account.dto.response.*;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.entity.Career;
import com.madeyepeople.pocketpt.domain.account.entity.MonthlyPtPrice;
import com.madeyepeople.pocketpt.domain.account.mapper.*;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.domain.account.repository.CareerRepository;
import com.madeyepeople.pocketpt.domain.account.repository.MonthlyPtPriceRepository;
import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatus;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtMatchingSummary;
import com.madeyepeople.pocketpt.domain.ptMatching.repository.PtMatchingRepository;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import com.madeyepeople.pocketpt.global.util.UniqueCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final PtMatchingRepository ptMatchingRepository;
    private final MonthlyPtPriceRepository monthlyPtPriceRepository;
    private final CareerRepository careerRepository;

    private final ToRegistrationResponse toRegistrationResponse;
    private final ToAccountGetResponse toAccountGetResponse;
    private final ToMonthlyPtPriceDtoList toMonthlyPtPriceDtoList;
    private final ToPtMatchingSummary toPtMatchingSummary;
    private final ToCareerEntity toCareerEntity;
    private final ToTrainerCareerCreateAndGetResponse toTrainerCareerCreateAndGetResponse;

    private final SecurityUtil securityUtil;
    private final UniqueCodeGenerator uniqueCodeGenerator;

    @Transactional
    public AccountRegistrationResponse registerAccount(CommonRegistrationRequest commonRegistrationRequest, String role) {
        Account account = securityUtil.getLoginAccountEntity();

        // 중복 회원가입시, 예외 발생
        if (account.getAccountRole() != null) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_REGISTERED, CustomExceptionMessage.ACCOUNT_ALREADY_REGISTERED.getMessage());
        }

        Account changed = account.updateByRegistrationRequest(
                commonRegistrationRequest.getName(),
                commonRegistrationRequest.getPhoneNumber(),
                commonRegistrationRequest.getNickname(),
                Role.valueOf(role.toUpperCase()),
                uniqueCodeGenerator.getUniqueCode()
        );

        // 트레이너일 경우, 월별 PT 단가가 필수로 입력되어야 함.
        if (account.getAccountRole() == Role.TRAINER) {
            if (commonRegistrationRequest.getMonthlyPtPriceList() == null) {
                throw new BusinessException(CustomExceptionMessage.TRAINER_MUST_HAVE_MONTHLY_PT_PRICE.getMessage());
            } else {
                List<MonthlyPtPriceDto> monthlyPtPriceList = commonRegistrationRequest.getMonthlyPtPriceList();
                for (MonthlyPtPriceDto monthlyPtPriceDto : monthlyPtPriceList) {
                    monthlyPtPriceRepository.save(MonthlyPtPrice.builder()
                            .account(changed)
                            .period(monthlyPtPriceDto.getPeriod())
                            .price(monthlyPtPriceDto.getPrice())
                            .build());
                }
            }
        }

        // TODO: save 과정에서 identificationCode 중복이 발생할 수 있음.
        //  Account entity에 unique constraint 적용해뒀으니 exception handling 필요. 다시 code 생성하던가.
        Account saved = accountRepository.save(changed);
        return toRegistrationResponse.fromAccountEntity(saved);
    }

    @Transactional
    public AccountDetailGetResponse getAccount() {
        Account account = securityUtil.getLoginAccountEntity();
        return toAccountGetResponse.fromAccountEntity(account);
    }

    @Transactional
    public CheckAccountSignupResponse checkSignup() {
        Account account = securityUtil.getLoginAccountEntity();
        return CheckAccountSignupResponse.builder()
                .isAccountSignedUp(account.getAccountRole() != null)
                .build();
    }

    @Transactional
    public MonthlyPtPriceGetResponse getTrainerPtPrice(String trainerCode) {
        Optional<Account> trainer = accountRepository.findByIdentificationCodeAndIsDeletedFalse(trainerCode);

        // 해당 Identification Code 가진 account가 있는지, 있다면 Role = trainer인지 확인
        if (trainer.isEmpty()) {
            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.TRAINER_IDENTIFICATION_CODE_NOT_FOUND.getMessage());
        } else if (!trainer.get().getAccountRole().getValue().equals("trainer")) {
            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.IDENTIFICATION_CODE_IS_NOT_TRAINER.getMessage());
        }

        List<MonthlyPtPriceDto> monthlyPtPriceDtoList = toMonthlyPtPriceDtoList.of(trainer.get().getMonthlyPtPriceList());

        return MonthlyPtPriceGetResponse.builder()
                .trainerAccountId(trainer.get().getAccountId())
                .monthlyPtPriceList(monthlyPtPriceDtoList)
                .build();
    }

    public TrainerTotalSalesGetResponse getTrainerTotalSales() {
        Account trainer = securityUtil.getLoginAccountEntity();
        List<PtMatching> ptMatchingList = ptMatchingRepository.findAllByTrainerAccountIdAndIsDeletedFalseAndStatusInOrderByCreatedAtDesc(
                trainer.getAccountId(), List.of(PtStatus.ACTIVE, PtStatus.EXPIRED)
        );


        return TrainerTotalSalesGetResponse.builder()
                .totalSales(trainer.getTotalSales())
                .ptMatchingSummaryList(ptMatchingList.stream()
                        .map(ptMatching -> toPtMatchingSummary.fromPtMatchingEntity(ptMatching, trainer.getAccountId()))
                        .toList())
                .build();
    }

    public TrainerTotalSalesGetResponse getTrainerMonthlySales(Integer year, Integer month) {
        Account trainer = securityUtil.getLoginAccountEntity();
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0).plusMonths(1).minusSeconds(1);

        List<PtMatching> ptMatchingList = ptMatchingRepository.findAllByTrainerAccountIdAndIsDeletedFalseAndCreatedAtBetweenAndStatusInOrderByCreatedAtDesc(
                trainer.getAccountId(), startOfMonth, endOfMonth, List.of(PtStatus.ACTIVE, PtStatus.EXPIRED)
        );

        Integer monthlySales = ptMatchingList.stream()
                .mapToInt(PtMatching::getPaymentAmount)
                .sum();

        return TrainerTotalSalesGetResponse.builder()
                .totalSales(monthlySales)
                .ptMatchingSummaryList(ptMatchingList.stream()
                        .map(ptMatching -> toPtMatchingSummary.fromPtMatchingEntity(ptMatching, trainer.getAccountId()))
                        .toList())
                .build();
    }

    public TrainerCareerCreateAndGetResponse createTrainerCareer(TrainerCareerCreateRequest trainerCareerCreateRequest) {
        Account trainer = securityUtil.getLoginAccountEntity();

        List<Career> savedCareerList = trainerCareerCreateRequest.getCareerList().stream()
                .map(career -> careerRepository.save(toCareerEntity.of(trainer, career)))
                .toList();

        return toTrainerCareerCreateAndGetResponse.of(savedCareerList);
    }

    public TrainerCareerCreateAndGetResponse getTrainerCareer() {
        Account trainer = securityUtil.getLoginAccountEntity();

        List<Career> careerList = careerRepository.findAllByTrainerAccountIdAndIsDeletedFalseOrderByType(trainer.getAccountId());

        return toTrainerCareerCreateAndGetResponse.of(careerList);
    }

    public CareerDto updateTrainerCareer(Long careerId, CareerUpdateDto careerUpdateDto) {
        Account trainer = securityUtil.getLoginAccountEntity();

        // 해당 careerId가 존재하지 않을 때
        Career career = careerRepository.findByCareerIdAndIsDeletedFalse(careerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRAINER_CAREER_ERROR, CustomExceptionMessage.CAREER_NOT_FOUND.getMessage()));

        // 해당 career의 trainerId와 로그인한 trainer의 id가 다를 때
        if (!career.getTrainer().getAccountId().equals(trainer.getAccountId())) {
            throw new BusinessException(ErrorCode.TRAINER_CAREER_ERROR, CustomExceptionMessage.CAREER_ACCOUNT_ID_IS_NOT_MATCHED.getMessage());
        }

        Career savedCareer = careerRepository.save(career.updateByCareerUpdateDto(careerUpdateDto));

        return CareerDto.builder()
                .careerId(savedCareer.getCareerId())
                .title(savedCareer.getTitle())
                .type(savedCareer.getType().getValue())
                .date(savedCareer.getDate())
                .build();
    }
}
