package com.madeyepeople.pocketpt.domain.ptMatching.util;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PaymentAmountCalculatorTest {

    private PaymentAmountCalculator paymentAmountCalculator = new PaymentAmountCalculator();

    @DisplayName("PT 가격 계산 테스트 - 단일 테스트 예시")
    @Test
    void calculate() {
        // given
        Integer subscriptionPeriod = 3;
        List<MonthlyPtPriceDto> monthlyPtPrices = List.of(
                MonthlyPtPriceDto.builder().monthlyPtPriceId(1L).period(1).price(50000).build(),
                MonthlyPtPriceDto.builder().monthlyPtPriceId(2L).period(3).price(40000).build(),
                MonthlyPtPriceDto.builder().monthlyPtPriceId(3L).period(6).price(30000).build()
        );

        // when
        Integer paymentAmount = paymentAmountCalculator.calculate(subscriptionPeriod, monthlyPtPrices);

        // then
        assertEquals(120000, paymentAmount);
    }

    @DisplayName("PT 가격 계산 테스트 - Parameterized 테스트 예시")
    @ParameterizedTest
    @MethodSource("provideCalculateParameter")
    void calculate2(Integer subscriptionPeriod, List<MonthlyPtPriceDto> monthlyPtPrices, Integer expectedPaymentAmount) {
        // given

        // when
        Integer calculatedPaymentAmount = paymentAmountCalculator.calculate(subscriptionPeriod, monthlyPtPrices);

        // then
        assertEquals(expectedPaymentAmount, calculatedPaymentAmount);
    }

    private static Stream<Arguments> provideCalculateParameter() {
        List<MonthlyPtPriceDto> price1 = List.of(
                MonthlyPtPriceDto.builder().monthlyPtPriceId(1L).period(1).price(50000).build(),
                MonthlyPtPriceDto.builder().monthlyPtPriceId(2L).period(3).price(40000).build(),
                MonthlyPtPriceDto.builder().monthlyPtPriceId(3L).period(6).price(30000).build()
        );
        return Stream.of(
                Arguments.of(1, price1, 50000),
                Arguments.of(2, price1, 100000),
                Arguments.of(4, price1, 160000),
                Arguments.of(5, price1, 200000),
                Arguments.of(6, price1, 180000),
                Arguments.of(7, price1, 210000)
        );
    }
}