package com.madeyepeople.pocketpt.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * [회원 가입시, 짧은 길이의 회원별 unique code 생성기]
 * UUID는 너무 길어 회원별 unique code로 사용하기 부적합하다.
 *
 * (현재 서버시간 + JVM running 시간)을 ase64로 인코딩함으로써 unique함을 보장하려한다.
 * 서버 시간만 사용하면 동시에 들어온 요청들을 구분할 수 없으므로 JVM running 시간을 더해준다.
 * (JVM running 시간은 nano 단위이므로 assembly cpu 명령어 실행단위까지 구분할 것이므로 동시 요청된 api 호출을 구분할 것이라 추측)
 *
 * 아래의 경우엔 unique 보장 못함
 * - 동일한 서버에 여러개의 JVM이 동시에 실행되는 경우
 * - 동시에 요청된 api들이 multi core에서 nano sec 단위로 동시에 실행되는 경우
 *
 * Created by San Kim on 2023-08-02.
 */
@Component
@RequiredArgsConstructor
public class UniqueCodeGenerator {

    public String getUniqueCode() {
        long currentTime = System.currentTimeMillis();
        long nanoTime = System.nanoTime();
        return longToBase64(currentTime + nanoTime);
    }
    public String longToBase64(long v) {
        final char[] digits = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
                'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z', '#', '$'
        };
        int shift = 6;
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << shift;
        long mask = radix - 1;
        long number = v;
        do {
            buf[--charPos] = digits[(int) (number & mask)];
            number >>>= shift;
        } while (number != 0);
        return new String(buf, charPos, (64 - charPos));
    }
}
