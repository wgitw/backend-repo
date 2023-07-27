package com.madeyepeople.pocketpt.domain.account.social;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.madeyepeople.pocketpt.domain.account.social.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedirectAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2 oAuth2 = new OAuth2();

    private final JwtUtil jwtUtil;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // TODO: FE 개발 안정화되면, FE와 정한 redirectUri만 가능하도록 수정
//        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
//            try {
//                throw new Exception("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
//            } catch (Exception e) {
//                log.error(e.getMessage());
//                throw new RuntimeException(e);
//            }
//        }

//        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        log.info("targetUrl-redirectUri: {}", targetUrl);

        String accessToken = jwtUtil.createAccessToken(authentication);
        log.info("accessToken: {}", accessToken);

        log.info("targetUrl-processedUri: {}", UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken)
                .build().toUriString());

        CookieUtil.addCookie(response, "accessToken", accessToken, 60 * 60 * 24);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return oAuth2.getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    log.error("authorizedURI: " + authorizedURI + "\n잘못된 redirectUri 입니다." );
                    return false;
                });
    }

    //TODO: FE와 accessToken을 cookie(안전)로 받을지, uri로 받을지 논의해서 코드 정리

//        AccountPrincipal user = (AccountPrincipal) authentication.getPrincipal();
//        String username = user.getUsername();
//        String referer = corsFrontend;
//
//        Collection<String> authorities = user.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
//
//        String accessToken = jwtUtil.createAccessToken(
//                user.getUsername(),
//                request.getRequestURL().toString(),
//                accessTokenExpire,
//                authorities);
//
//        String refreshToken = jwtUtil.createRefreshToken(
//                user.getUsername(),
//                request.getRequestURL().toString(),
//                refreshTokenExpire);
//
////        ResponseCookie cookie_refresh = ResponseCookie.from(jwtUtil.REFRESH_TOKEN, refreshToken)
////                .httpOnly(true)
////                .secure(false)
////                .path("/")      // path
////                .maxAge(Duration.ofDays(15))
////                .sameSite("None")  // sameSite
////                .build();
////        ResponseCookie cookie_access = ResponseCookie.from("access-token", accessToken)
////                .httpOnly(true)
////                .secure(false)
////                .path("/")      // path
////                .maxAge(Duration.ofDays(15))
////                .sameSite("None")  // sameSite
////                .build();
////
////        response.addHeader(HttpHeaders.SET_COOKIE, cookie_refresh.toString());
////        response.addHeader(HttpHeaders.SET_COOKIE, cookie_access.toString());
////        response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
//        log.info("accessToken: " + accessToken);
//        log.info("refreshToken: " + refreshToken);
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setContentType("text/plain");
//        response.setCharacterEncoding("utf-8");
//
//        Map<String, String> map = Map.of("accessToken", accessToken, "refreshToken", refreshToken);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(map);
////        log.info(json);
//        response.getWriter().write(json);
//
//
//        Cookie cookie_refresh = new Cookie(jwtUtil.COOKIE_KEY_REFRESH_TOKEN, refreshToken);
//        cookie_refresh.setDomain("pocketpt.shop");
//        cookie_refresh.setPath("/");
//        cookie_refresh.setSecure(true);
//        cookie_refresh.setAttribute("SameSite", "None");
//        cookie_refresh.setAttribute("refresh_token", refreshToken);
//        response.addCookie(cookie_refresh);
//
//
//        Cookie cookie_access = new Cookie(jwtUtil.COOKIE_KEY_ACCESS_TOKEN, accessToken);
//        cookie_access.setDomain("pocketpt.shop");
//        cookie_access.setPath("/");
//        cookie_access.setSecure(true);
//        cookie_access.setAttribute("SameSite", "None");
//        cookie_access.setAttribute("access_token", accessToken);
//        response.addCookie(cookie_access);
//
//        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
//        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
//        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, corsFrontend);
//
//        response.sendRedirect(referer);
//    }
}
