package com.madeyepeople.pocketpt.domain.account.service;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import com.madeyepeople.pocketpt.domain.account.dto.CareerUpdateDto;
import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.dto.request.CommonRegistrationRequest;
import com.madeyepeople.pocketpt.domain.account.dto.request.TrainerCareerCreateRequest;
import com.madeyepeople.pocketpt.domain.account.dto.request.TrainerMonthlyPtPriceCreateRequest;
import com.madeyepeople.pocketpt.domain.account.dto.response.*;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.entity.Career;
import com.madeyepeople.pocketpt.domain.account.entity.MonthlyPtPrice;
import com.madeyepeople.pocketpt.domain.account.mapper.*;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.domain.account.repository.CareerRepository;
import com.madeyepeople.pocketpt.domain.account.repository.MonthlyPtPriceRepository;
import com.madeyepeople.pocketpt.domain.account.util.TrainerMonthlyPtPriceUtil;
import com.madeyepeople.pocketpt.domain.admin.service.FixedPlatformFeePolicy;
import com.madeyepeople.pocketpt.domain.admin.service.RelativePlatformFeePolicy;
import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatus;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtMatchingSummary;
import com.madeyepeople.pocketpt.domain.ptMatching.repository.PtMatchingRepository;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import com.madeyepeople.pocketpt.global.util.UniqueCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final FixedPlatformFeePolicy fixedPlatformFeePolicy;
    private final RelativePlatformFeePolicy relativePlatformFeePolicy;

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
    private final ToMonthlyPtPriceEntity toMonthlyPtPriceEntity;
    private final ToMonthlyPtPriceDto toMonthlyPtPriceDto;

    private final SecurityUtil securityUtil;
    private final TrainerMonthlyPtPriceUtil trainerMonthlyPtPriceUtil;
    private final UniqueCodeGenerator uniqueCodeGenerator;

    @Transactional
    public AccountRegistrationResponse registerAccount(CommonRegistrationRequest commonRegistrationRequest, String role) {
        Account account = securityUtil.getLoginAccountEntity();

        // 중복 회원가입시, 예외 발생
        if (account.getAccountRole() != null) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_REGISTERED, CustomExceptionMessage.ACCOUNT_ALREADY_REGISTERED.getMessage());
        }

        String uniqueCode;

        // [테스트] 김일곤의 경우, uniqueCode 고정
        if (account.getEmail().equals("jesus0321@naver.com")) {
            uniqueCode = "lhGbiEGe";
        } else {
            uniqueCode = uniqueCodeGenerator.getUniqueCode();
        }

        Account changed = account.updateByRegistrationRequest(
                commonRegistrationRequest.getName(),
                commonRegistrationRequest.getPhoneNumber(),
                Role.valueOf(role.toUpperCase()),
                uniqueCode
        );

        // 트레이너일 경우
        if (account.getAccountRole() == Role.TRAINER) {
            // 월별 PT 단가가 필수로 입력되어야 함.
            if (commonRegistrationRequest.getMonthlyPtPriceList().isEmpty()) {
                throw new BusinessException(ErrorCode.TRAINER_MONTHLY_PT_PRICE_ERROR, CustomExceptionMessage.TRAINER_MUST_HAVE_MONTHLY_PT_PRICE.getMessage());
            // 중복되는 개월수가 없어야함.
            } else if (trainerMonthlyPtPriceUtil.hasDuplicatePeriodByDto(commonRegistrationRequest.getMonthlyPtPriceList())) {
                throw new BusinessException(ErrorCode.TRAINER_MONTHLY_PT_PRICE_ERROR, CustomExceptionMessage.MONTHLY_PT_PRICE_DUPLICATED_PERIOD.getMessage());
            } else {
                List<MonthlyPtPriceDto> monthlyPtPriceDtoList = commonRegistrationRequest.getMonthlyPtPriceList();
                for (MonthlyPtPriceDto monthlyPtPriceDto : monthlyPtPriceDtoList) {
                    monthlyPtPriceRepository.save(MonthlyPtPrice.builder()
                            .trainer(changed)
                            .period(monthlyPtPriceDto.getPeriod())
                            .price(monthlyPtPriceDto.getPrice())
                            .build());
                }
            }
        }

        // TODO: save 과정에서 identificationCode 중복이 발생할 수 있음. 랜덤 코드 생성 로직 상, 극악의 확률로 중복 가능
        //  Account entity에 unique constraint 적용해뒀으니 exception handling 필요. 다시 code 생성하던가.
        Account saved = accountRepository.save(changed);
        return toRegistrationResponse.fromAccountEntity(saved);
    }

    @Transactional(readOnly = true)
    public AccountDetailGetResponse getAccount() {
        Account account = securityUtil.getLoginAccountEntity();
        return toAccountGetResponse.fromAccountEntity(account);
    }

    @Transactional(readOnly = true)
    public CheckAccountSignupResponse checkSignup() {
        Account account = securityUtil.getLoginAccountEntity();
        return CheckAccountSignupResponse.builder()
                .isAccountSignedUp(account.getAccountRole() != null)
                .build();
    }

    @Transactional(readOnly = true)
    public MonthlyPtPriceGetResponse getTrainerAllPtPrice(String trainerCode) {
        Optional<Account> trainer = accountRepository.findByIdentificationCodeAndIsDeletedFalse(trainerCode);

        // 해당 Identification Code 가진 account가 있는지, 있다면 Role = trainer인지 확인
        if (trainer.isEmpty()) {
            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.TRAINER_IDENTIFICATION_CODE_NOT_FOUND.getMessage());
        } else if (!trainer.get().getAccountRole().getValue().equals("trainer")) {
            throw new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.IDENTIFICATION_CODE_IS_NOT_TRAINER.getMessage());
        }

        List<MonthlyPtPriceDto> monthlyPtPriceDtoList = toMonthlyPtPriceDtoList.of(trainer.get().getMonthlyPtPriceList());
        trainerMonthlyPtPriceUtil.sortByPeriod(monthlyPtPriceDtoList);

        return MonthlyPtPriceGetResponse.builder()
                .trainerAccountId(trainer.get().getAccountId())
                .monthlyPtPriceList(monthlyPtPriceDtoList)
                .build();
    }

    @Transactional
    public MonthlyPtPriceDto createTrainerMonthlyPtPrice(TrainerMonthlyPtPriceCreateRequest trainerMonthlyPtPriceCreateRequest) {
        Account trainer = securityUtil.getLoginTrainerEntity();

        MonthlyPtPrice requestedMonthlyPtPrice = toMonthlyPtPriceEntity.of(trainer, trainerMonthlyPtPriceCreateRequest);
        List<MonthlyPtPrice> monthlyPtPriceList = trainer.getMonthlyPtPriceList();
        monthlyPtPriceList.add(requestedMonthlyPtPrice);

        if (trainerMonthlyPtPriceUtil.hasDuplicatePeriodByEntity(monthlyPtPriceList)) {
            throw new BusinessException(ErrorCode.TRAINER_MONTHLY_PT_PRICE_ERROR, CustomExceptionMessage.MONTHLY_PT_PRICE_DUPLICATED_PERIOD.getMessage());
        }

        MonthlyPtPrice monthlyPtPrice = monthlyPtPriceRepository.save(requestedMonthlyPtPrice);

        return toMonthlyPtPriceDto.of(monthlyPtPrice);
    }

    @Transactional(readOnly = true)
    public TrainerTotalSalesGetResponse getTrainerTotalSales() {
        Account trainer = securityUtil.getLoginTrainerEntity();

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

    @Transactional(readOnly = true)
    public TrainerTotalSalesGetResponse getTrainerMonthlySales(Integer year, Integer month) {
        Account trainer = securityUtil.getLoginTrainerEntity();

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

    @Transactional
    public TrainerCareerCreateAndGetResponse createTrainerCareer(TrainerCareerCreateRequest trainerCareerCreateRequest) {
        Account trainer = securityUtil.getLoginTrainerEntity();

        List<Career> savedCareerList = trainerCareerCreateRequest.getCareerList().stream()
                .map(career -> careerRepository.save(toCareerEntity.of(trainer, career)))
                .toList();

        return toTrainerCareerCreateAndGetResponse.of(savedCareerList);
    }

    @Transactional(readOnly = true)
    public TrainerCareerCreateAndGetResponse getTrainerCareer() {

        Account trainer = securityUtil.getLoginTrainerEntity();

        List<Career> careerList = careerRepository.findAllByTrainerAccountIdAndIsDeletedFalseOrderByType(trainer.getAccountId());

        return toTrainerCareerCreateAndGetResponse.of(careerList);
    }

    @Transactional
    public CareerDto updateTrainerCareer(Long careerId, CareerUpdateDto careerUpdateDto) {
        Account trainer = securityUtil.getLoginTrainerEntity();

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

    @Transactional
    public void deleteTrainerCareer(Long careerId) {
        Account trainer = securityUtil.getLoginTrainerEntity();

        // 해당 careerId가 존재하지 않을 때
        Career career = careerRepository.findByCareerIdAndIsDeletedFalse(careerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRAINER_CAREER_ERROR, CustomExceptionMessage.CAREER_NOT_FOUND.getMessage()));

        // 해당 career의 trainerId와 로그인한 trainer의 id가 다를 때
        if (!career.getTrainer().getAccountId().equals(trainer.getAccountId())) {
            throw new BusinessException(ErrorCode.TRAINER_CAREER_ERROR, CustomExceptionMessage.CAREER_ACCOUNT_ID_IS_NOT_MATCHED.getMessage());
        }

        careerRepository.delete(career);
    }

    @Transactional(readOnly = true)
    public TrainerIncomeGetResponse getTrainerIncome(Integer sales, String plan) {
        Integer income;

        if (plan.equals("fixed")) {
            income = fixedPlatformFeePolicy.calculateIncome(sales);
        } else {
            income = relativePlatformFeePolicy.calculateIncome(sales);
        }

        return TrainerIncomeGetResponse.builder()
                .income(income)
                .build();
    }

    // 테스트용 api
    @Transactional
    public String removeRoleAndMonthlyPtPrice() {
        Account account = securityUtil.getLoginAccountEntity();
        monthlyPtPriceRepository.deleteAllByTrainerAccountId(account.getAccountId());
        Account saved = accountRepository.save(account.deleteAccountRole());
        return saved.toString();
    }
}
