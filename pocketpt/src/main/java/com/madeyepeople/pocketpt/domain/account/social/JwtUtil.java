package com.madeyepeople.pocketpt.domain.account.social;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    public final String COOKIE_KEY_ACCESS_TOKEN = "access_token";
    public final String COOKIE_KEY_REFRESH_TOKEN = "refresh_token";

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpire;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication) {
        AccountPrincipal user = (AccountPrincipal) authentication.getPrincipal();
        Collection<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", new ArrayList<>(authorities));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpire);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // TODO: refreshToken 필요성 재고 필요
    public String createRefreshToken(String subject, String requestUrl) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpire);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuer(requestUrl)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("authorities") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("authorities").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        return usernamePasswordAuthenticationToken;
    }

    public String extractJwt(final StompHeaderAccessor accessor) {
        String bearerToken = accessor.getFirstNativeHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        // TODO: 각 경우에 따라 다른 exception 처리 필요. 일단 login으로 redirect
        } catch (SignatureException e) {
            throw new JwtException("사용자 인증 실패", e);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new JwtException("유효하지 않은 토큰", e);
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 JWT Token", e);
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원하지 않는 JWT token", e);
        } catch (IllegalArgumentException e) {
            throw new JwtException("지원하지 않는 JWT token", e);
        } catch (Exception e) {
            throw new JwtException("알 수 없는 JWT token error", e);
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
