package com.madeyepeople.pocketpt.domain.account.social;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RedirectAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${cors.frontend}")
    private String corsFrontend;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpire;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    private final JwtUtil jwtUtil;

//    @Autowired
//    public RedirectAuthenticationSuccessHandler(@Value("${cors.frontend}") String corsFrontend,
//                                                @Value("${jwt.access-token-expire}") long accessTokenExpire,
//                                                @Value("${jwt.refresh-token-expire}") long refreshTokenExpire) {
//        this.corsFrontend = corsFrontend;
//        this.accessTokenExpire = accessTokenExpire;
//        this.refreshTokenExpire = refreshTokenExpire;
//    }

    /**
     *     oauth 로그인 성공시 JWT Token 생성해서 리다이렉트 응답.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        AccountPrincipal user = (AccountPrincipal) authentication.getPrincipal();
        String referer = corsFrontend;

        Collection<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String accessToken = jwtUtil.createAccessToken(
                request.getRequestURL().toString(),
                user.getUsername(),
                accessTokenExpire,
                authorities);

        String refreshToken = jwtUtil.createRefreshToken(
                request.getRequestURL().toString(),
                user.getUsername(),
                refreshTokenExpire);

        ResponseCookie cookie = ResponseCookie.from(jwtUtil.REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")      // path
                .maxAge(Duration.ofDays(15))
                .sameSite("None")  // sameSite
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect(
                referer +
                        "?access_token=" + accessToken);
    }
}
