package com.madeyepeople.pocketpt.domain.account.service;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.dto.request.CommonRegistrationRequest;
import com.madeyepeople.pocketpt.domain.account.dto.response.AccountDetailGetResponse;
import com.madeyepeople.pocketpt.domain.account.dto.response.AccountRegistrationResponse;
import com.madeyepeople.pocketpt.domain.account.dto.response.CheckAccountSignupResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.entity.MonthlyPtPrice;
import com.madeyepeople.pocketpt.domain.account.mapper.ToAccountGetResponse;
import com.madeyepeople.pocketpt.domain.account.mapper.ToRegistrationResponse;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.domain.account.repository.MonthlyPtPriceRepository;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import com.madeyepeople.pocketpt.global.util.UniqueCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final MonthlyPtPriceRepository monthlyPtPriceRepository;

    private final ToRegistrationResponse toRegistrationResponse;
    private final ToAccountGetResponse toAccountGetResponse;

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
                uniqueCodeGenerator.getUniqueCode(),
                commonRegistrationRequest.getMonthlyPtPriceList().stream()
                        .map(monthlyPtPriceDto -> MonthlyPtPrice.builder()
                                .period(monthlyPtPriceDto.getPeriod())
                                .price(monthlyPtPriceDto.getPrice())
                                .build())
                        .collect(Collectors.toList())
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
}
