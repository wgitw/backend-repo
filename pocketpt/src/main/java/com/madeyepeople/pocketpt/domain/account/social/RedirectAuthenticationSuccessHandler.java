package com.madeyepeople.pocketpt.domain.account.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
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
                user.getUsername(),
                request.getRequestURL().toString(),
                accessTokenExpire,
                authorities);

        String refreshToken = jwtUtil.createRefreshToken(
                user.getUsername(),
                request.getRequestURL().toString(),
                refreshTokenExpire);

//        ResponseCookie cookie_refresh = ResponseCookie.from(jwtUtil.REFRESH_TOKEN, refreshToken)
//                .httpOnly(true)
//                .secure(false)
//                .path("/")      // path
//                .maxAge(Duration.ofDays(15))
//                .sameSite("None")  // sameSite
//                .build();
//        ResponseCookie cookie_access = ResponseCookie.from("access-token", accessToken)
//                .httpOnly(true)
//                .secure(false)
//                .path("/")      // path
//                .maxAge(Duration.ofDays(15))
//                .sameSite("None")  // sameSite
//                .build();
//
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie_refresh.toString());
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie_access.toString());
//        response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        log.info("accessToken: " + accessToken);
        log.info("refreshToken: " + refreshToken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");

        Map<String, String> map = Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(map);
        log.info(json);
        response.getWriter().write(json);

        Cookie cookie_refresh = new Cookie(jwtUtil.COOKIE_KEY_REFRESH_TOKEN, refreshToken);
        cookie_refresh.setDomain("pocketpt.netlify.app");
        cookie_refresh.setPath("/");
        cookie_refresh.setSecure(true);
        cookie_refresh.setAttribute("SameSite", "None");
        cookie_refresh.setAttribute("refresh_token", refreshToken);
        response.addCookie(cookie_refresh);

        Cookie cookie_access = new Cookie(jwtUtil.COOKIE_KEY_ACCESS_TOKEN, accessToken);
//        log.error(cookie_access.getDomain());
        cookie_access.setDomain("pocketpt.netlify.app");
        cookie_access.setPath("/");
        cookie_access.setSecure(true);
        cookie_access.setAttribute("SameSite", "None");
        cookie_access.setAttribute("access_token", accessToken);
        response.addCookie(cookie_access);

        log.error(cookie_access.getPath());
        log.error(cookie_access.getAttributes().toString());
        log.error(cookie_refresh.getAttributes().toString());

        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, corsFrontend);
        response.sendRedirect(referer);
    }
}
