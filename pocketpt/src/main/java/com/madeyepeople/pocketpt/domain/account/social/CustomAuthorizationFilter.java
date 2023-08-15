package com.madeyepeople.pocketpt.domain.account.social;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            // 1. Request Header 에서 JWT 토큰 추출
            String token = resolveToken((HttpServletRequest) request);

            // 2. validateToken 으로 토큰 유효성 검사
            if (token != null && jwtUtil.validateToken(token)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Security Context에서 사용자 인증을 설정할 수 없습니다.", e);
        }
        chain.doFilter(request, response);
    }

    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
//        Enumeration<String> headerNames = request.getHeaderNames();
//        log.info("Header List");
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            String headerValue = request.getHeader(headerName);
//            log.info(headerName + " : " + headerValue);
//        }
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            try {
                return bearerToken.substring(7);
            } catch (StringIndexOutOfBoundsException e) {
                log.error(e.getMessage());
                throw new JwtException("Bearer prefix 이후에 토큰이 존재하지 않습니다.", e);
            }
        }
        return null;
    }
}