package com.madeyepeople.pocketpt.domain.account.service;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.dto.*;
import com.madeyepeople.pocketpt.domain.account.dto.request.*;
import com.madeyepeople.pocketpt.domain.account.dto.response.*;
import com.madeyepeople.pocketpt.domain.account.entity.*;
import com.madeyepeople.pocketpt.domain.account.mapper.*;
import com.madeyepeople.pocketpt.domain.account.repository.*;
import com.madeyepeople.pocketpt.domain.account.social.JwtUtil;
import com.madeyepeople.pocketpt.domain.account.util.TrainerMonthlyPtPriceUtil;
import com.madeyepeople.pocketpt.domain.admin.constant.ServiceLogicConstant;
import com.madeyepeople.pocketpt.domain.admin.service.FixedPlatformFeePolicy;
import com.madeyepeople.pocketpt.domain.admin.service.RelativePlatformFeePolicy;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.domain.chattingParticipant.repository.ChattingParticipantRepository;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.domain.chattingRoom.repository.ChattingRoomRepository;
import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatus;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import com.madeyepeople.pocketpt.domain.ptMatching.mapper.ToPtMatchingSummary;
import com.madeyepeople.pocketpt.domain.ptMatching.repository.PtMatchingRepository;
import com.madeyepeople.pocketpt.domain.ptMatching.service.PtMatchingService;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.s3.S3FileService;
import com.madeyepeople.pocketpt.global.util.RedisUtil;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import com.madeyepeople.pocketpt.global.util.UniqueCodeGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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

    private final PtMatchingService ptMatchingService;

    private final AccountRepository accountRepository;
    private final PtMatchingRepository ptMatchingRepository;
    private final MonthlyPtPriceRepository monthlyPtPriceRepository;
    private final CareerRepository careerRepository;
    private final PurposeRepository purposeRepository;
    private final PhysicalInfoRepository physicalInfoRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingParticipantRepository chattingParticipantRepository;

    private final ToRegistrationResponse toRegistrationResponse;
    private final ToAccountGetResponse toAccountGetResponse;
    private final ToMonthlyPtPriceDtoList toMonthlyPtPriceDtoList;
    private final ToPtMatchingSummary toPtMatchingSummary;
    private final ToCareerEntity toCareerEntity;
    private final ToTrainerCareerCreateAndGetResponse toTrainerCareerCreateAndGetResponse;
    private final ToMonthlyPtPriceEntity toMonthlyPtPriceEntity;
    private final ToMonthlyPtPriceDto toMonthlyPtPriceDto;
    private final ToPurposeEntity toPurposeEntity;
    private final ToPurposeDto toPurposeDto;
    private final ToProfileGetResponse toProfileGetResponse;
    private final ToPhysicalInfoDto toPhysicalInfoDto;
    private final ToPhysicalInfoEntity toPhysicalInfoEntity;
    private final ToPhysicalInfoDtoList toPhysicalInfoDtoList;

    private final S3FileService s3FileService;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
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
            // TODO: 0개월 0원 같은 invalid한 period, price에 대한 validation 필요
            // 월별 PT 단가가 필수로 입력되어야 함.
            if (commonRegistrationRequest.getMonthlyPtPriceList() == null || commonRegistrationRequest.getMonthlyPtPriceList().isEmpty()) {
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

    @Transactional
    public void logout(HttpServletRequest httpServletRequest) {
        String accessToken = httpServletRequest.getHeader("Authorization").substring(7);

        redisUtil.set(accessToken, "logout", jwtUtil.getExpiration(accessToken) + 1000);
    }

    @Transactional
    public WithdrawalResponse withdrawal(HttpServletRequest httpServletRequest) {
        Account account = securityUtil.getLoginAccountEntity();
        String accessToken = httpServletRequest.getHeader("Authorization").substring(7);

        // 채팅 정보 삭제
        // [1] chattingParticipant에서 해당 회원이 참여하고 있는 채팅방 리스트 가져오기
        List<ChattingParticipant> foundChattingParticipantList = account.getChattingParticipantList();

        // [2] 채팅방 삭제 및 채팅방에 포함되어 있는 채팅참여자 정보 삭제
        for (ChattingParticipant chattingParticipant : foundChattingParticipantList) {
            // [2-1] 채팅참여자의 채팅방 정보 가져오기
            ChattingRoom chattingRoom = chattingParticipant.getChattingRoom();

            // [2-2] 채팅방에 속한 참여자 리스트 가져오기
            List<ChattingParticipant> chattingParticipantList = chattingParticipantRepository.findByChattingRoomAndIsDeletedFalse(chattingRoom);

            // [2-3] 채팅방에 속한 참여자들의 isDeleted를 true로 변경
            for (ChattingParticipant c : chattingParticipantList) {
                c.setIsDeleted(true);
                chattingParticipantRepository.save(c);
            }

            // [2-4] 채팅방 삭제
            chattingRoom.setIsDeleted(true);
            chattingRoomRepository.save(chattingRoom);
        }

        redisUtil.set(accessToken, "withdrawal", jwtUtil.getExpiration(accessToken) + 1000);
        accountRepository.deleteByAccountId(account.getAccountId());

        return WithdrawalResponse.builder()
                .accountId(account.getAccountId())
                .name(account.getName())
                .build();
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
                .defaultName(account.getName())
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
    public MonthlyPtPriceDto createTrainerMonthlyPtPrice(TrainerMonthlyPtPriceCreateAndUpdateRequest trainerMonthlyPtPriceCreateAndUpdateRequest) {
        Account trainer = securityUtil.getLoginTrainerEntity();

        MonthlyPtPrice requestedMonthlyPtPrice = toMonthlyPtPriceEntity.of(trainer, trainerMonthlyPtPriceCreateAndUpdateRequest);
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
    public TrainerCareerCreateAndGetResponse getTrainerCareer(Long accountId) {
        List<Career> careerList = careerRepository.findAllByTrainerAccountIdAndIsDeletedFalseOrderByType(accountId);

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

    @Transactional
    public MonthlyPtPriceDto updateTrainerMonthlyPtPrice(Long ptPriceId, TrainerMonthlyPtPriceCreateAndUpdateRequest trainerMonthlyPtPriceCreateAndUpdateRequest) {
        Account trainer = securityUtil.getLoginTrainerEntity();

        // 해당 ptPriceId가 존재하지 않을 때
        MonthlyPtPrice monthlyPtPrice = monthlyPtPriceRepository.findById(ptPriceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRAINER_MONTHLY_PT_PRICE_ERROR, CustomExceptionMessage.MONTHLY_PT_PRICE_NOT_FOUND.getMessage()));

        // 요청된 ptPriceId가 로그인한 trainer의 ptPrice인지 확인
        if (!monthlyPtPrice.getTrainer().equals(trainer)) {
            throw new BusinessException(ErrorCode.TRAINER_MONTHLY_PT_PRICE_ERROR, CustomExceptionMessage.MONTHLY_PT_PRICE_ACCOUNT_ID_IS_NOT_MATCHED.getMessage());
        }

        List<MonthlyPtPrice> updatedMonthlyPtPriceList = trainer.getMonthlyPtPriceList();
        for (MonthlyPtPrice nowMonthlyPtPrice : updatedMonthlyPtPriceList) {
            if (nowMonthlyPtPrice.getMonthlyPtPriceId().equals(ptPriceId)) {
                log.info(nowMonthlyPtPrice.toString());
                nowMonthlyPtPrice.updateByTrainerMonthlyPtPriceCreateAndUpdateRequest(trainerMonthlyPtPriceCreateAndUpdateRequest);
            }
        }

        log.info(updatedMonthlyPtPriceList.toString());

        // 요청된 period가 이미 존재하는 period인지 확인
        if (trainerMonthlyPtPriceUtil.hasDuplicatePeriodByEntity(updatedMonthlyPtPriceList)) {
            throw new BusinessException(ErrorCode.TRAINER_MONTHLY_PT_PRICE_ERROR, CustomExceptionMessage.MONTHLY_PT_PRICE_DUPLICATED_PERIOD.getMessage());
        }

        monthlyPtPrice.updateByTrainerMonthlyPtPriceCreateAndUpdateRequest(trainerMonthlyPtPriceCreateAndUpdateRequest);
        MonthlyPtPrice savedMonthlyPtPrice = monthlyPtPriceRepository.save(monthlyPtPrice);

        return toMonthlyPtPriceDto.of(savedMonthlyPtPrice);
    }

    @Transactional
    public String deleteTrainerMonthlyPtPrice(Long ptPriceId) {
        Account trainer = securityUtil.getLoginTrainerEntity();

        // 해당 ptPriceId가 존재하지 않을 때
        MonthlyPtPrice monthlyPtPrice = monthlyPtPriceRepository.findById(ptPriceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRAINER_MONTHLY_PT_PRICE_ERROR, CustomExceptionMessage.MONTHLY_PT_PRICE_NOT_FOUND.getMessage()));

        // 요청된 ptPriceId가 로그인한 trainer의 ptPrice인지 확인
        if (!monthlyPtPrice.getTrainer().equals(trainer)) {
            throw new BusinessException(ErrorCode.TRAINER_MONTHLY_PT_PRICE_ERROR, CustomExceptionMessage.MONTHLY_PT_PRICE_ACCOUNT_ID_IS_NOT_MATCHED.getMessage());
        }

        monthlyPtPriceRepository.delete(monthlyPtPrice);

        return "deleted ptPriceId = " + ptPriceId;
    }

    @Transactional
    public PurposeDto createPurpose(PurposeCreateRequest purposeCreateRequest) {
        Account account = securityUtil.getLoginAccountEntity();

        // 회원의 purpose 개수가 최대치(3개)라면 생성 불가
        List<Purpose> purposeList = account.getPurposeList();
        if (purposeList.size() == ServiceLogicConstant.MAXIMUM_NUMBER_OF_PURPOSE) {
            throw new BusinessException(ErrorCode.ACCOUNT_PURPOSE_ERROR, CustomExceptionMessage.ACCOUNT_PURPOSE_COUNT_IS_FULL.getMessage());
        }

        Purpose saved = purposeRepository.save(toPurposeEntity.of(account, purposeCreateRequest));

        return toPurposeDto.of(saved);
    }

    @Transactional(readOnly = true)
    public List<PurposeDto> getPurpose(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_PURPOSE_ERROR, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage()));

        return toPurposeDto.of(account.getPurposeList());
    }

    @Transactional
    public PurposeDto updatePurpose(Long purposeId, PurposeUpdateRequest purposeUpdateRequest) {
        Account account = securityUtil.getLoginAccountEntity();

        // 해당 purposeId가 존재하지 않을 때
        Purpose purpose = purposeRepository.findByPurposeIdAndIsDeletedFalse(purposeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_PURPOSE_ERROR, CustomExceptionMessage.PURPOSE_NOT_FOUND.getMessage()));

        // 해당 purpose의 accountId와 로그인한 account의 id가 다를 때
        if (!purpose.getAccount().getAccountId().equals(account.getAccountId())) {
            throw new BusinessException(ErrorCode.ACCOUNT_PURPOSE_ERROR, CustomExceptionMessage.PURPOSE_ACCOUNT_ID_IS_NOT_MATCHED.getMessage());
        }

        purpose.updateByPurposeUpdateRequest(purposeUpdateRequest);
        Purpose saved = purposeRepository.save(purpose);

        return toPurposeDto.of(saved);
    }

    @Transactional
    public String deletePurpose(Long purposeId) {
        Account account = securityUtil.getLoginAccountEntity();

        // 해당 purposeId가 존재하지 않을 때
        Purpose purpose = purposeRepository.findByPurposeIdAndIsDeletedFalse(purposeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_PURPOSE_ERROR, CustomExceptionMessage.PURPOSE_NOT_FOUND.getMessage()));

        // 해당 purpose의 accountId와 로그인한 account의 id가 다를 때
        if (!purpose.getAccount().getAccountId().equals(account.getAccountId())) {
            throw new BusinessException(ErrorCode.ACCOUNT_PURPOSE_ERROR, CustomExceptionMessage.PURPOSE_ACCOUNT_ID_IS_NOT_MATCHED.getMessage());
        }

        purposeRepository.delete(purpose);

        return "deleted purposeId = " + purposeId;
    }

    @Transactional(readOnly = true)
    public ProfileGetResponse getProfile(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage()));

        return toProfileGetResponse.of(account);
    }

    public String flushRedis() {
        redisUtil.flushAll();
        return "flushed";
    }

    @Transactional
    public AccountUpdateResponse updateIntroduce(AccountUpdateRequest accountUpdateRequest) {
        Account account = securityUtil.getLoginAccountEntity();
        account.updateByAccountUpdateRequest(accountUpdateRequest);
        Account updatedAccount = accountRepository.save(account);
        return AccountUpdateResponse.builder()
                .introduce(updatedAccount.getIntroduce())
                .build();
    }

    @Transactional
    public AccountUpdateResponse updateProfilePicture(AccountProfilePictureUpdateRequest accountProfilePictureUpdateRequest) {
        Account account = securityUtil.getLoginAccountEntity();
        String fileUrl = s3FileService.uploadFile("profile/" + account.getAccountId() + "/", accountProfilePictureUpdateRequest.getFile());
        account.updateByProfilePictureUrl(fileUrl);
        Account updatedAccount = accountRepository.save(account);
        return AccountUpdateResponse.builder()
                .profileImageUrl(updatedAccount.getProfilePictureUrl())
                .build();
    }

    @Transactional
    public PhysicalInfoDto createPhysicalInfo(PhysicalInfoCreateRequest physicalInfoCreateRequest) {
        Account account = securityUtil.getLoginAccountEntity();

        // (accountId, date)가 unique 한지 확인
        if (physicalInfoRepository.existsByAccountAccountIdAndDate(account.getAccountId(), physicalInfoCreateRequest.getDate())) {
            throw new BusinessException(ErrorCode.PHYSICAL_INFO_ERROR, CustomExceptionMessage.PHYSICAL_INFO_ALREADY_EXISTS.getMessage());
        }

        PhysicalInfo physicalInfo = physicalInfoRepository.save(toPhysicalInfoEntity.of(account, physicalInfoCreateRequest));

        return toPhysicalInfoDto.of(physicalInfo);
    }

    public List<PhysicalInfoDto> getPhysicalInfo(Long accountId) {
        Account loginAccount = securityUtil.getLoginAccountEntity();
        Account targetAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage()));

        // targetAccount가 로그인한 account가 아니라면(본인꺼 조회가 아니라면), loginAccount가 targetAccount와 PT 매칭 status=ACTIVE 이어야 함.
        if (!loginAccount.getAccountId().equals(targetAccount.getAccountId())) {
            if (!ptMatchingService.isTheirPtMatchingStatusActive(loginAccount, targetAccount)) {
                throw new BusinessException(ErrorCode.PHYSICAL_INFO_ERROR, CustomExceptionMessage.PHYSICAL_INFO_GET_REQUESTER_IS_NOT_VALID.getMessage());
            }
        }

        List<PhysicalInfo> physicalInfoList = physicalInfoRepository.findAllByAccountAccountIdAndIsDeletedFalseOrderByDateAsc(accountId);

        return toPhysicalInfoDtoList.of(physicalInfoList);
    }

    // TODO: 신체 정보 수정 API
//    @Transactional
//    public PhysicalInfoDto updatePhysicalInfo(Long physicalInfoId, PhysicalInfoUpdateRequest physicalInfoUpdateRequest) {
//        Account account = securityUtil.getLoginAccountEntity();
//
//        // 해당 physicalInfoId가 존재하지 않을 때
//        PhysicalInfo physicalInfo = physicalInfoRepository.findByPhysicalInfoIdAndIsDeletedFalse(physicalInfoId)
//                .orElseThrow(() -> new BusinessException(ErrorCode.PHYSICAL_INFO_ERROR, CustomExceptionMessage.PHYSICAL_INFO_NOT_FOUND.getMessage()));
//
//        // 해당 physicalInfo의 accountId와 로그인한 account의 id가 다를 때
//        if (!physicalInfo.getAccount().getAccountId().equals(account.getAccountId())) {
//            throw new BusinessException(ErrorCode.PHYSICAL_INFO_ERROR, CustomExceptionMessage.PHYSICAL_INFO_ACCOUNT_ID_IS_NOT_MATCHED.getMessage());
//        }
//
//        physicalInfo.updateByPhysicalInfoUpdateRequest(physicalInfoUpdateRequest);
//        PhysicalInfo saved = physicalInfoRepository.save(physicalInfo);
//
//        return toPhysicalInfoDto.of(saved);
//    }
}
