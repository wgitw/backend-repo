package com.madeyepeople.pocketpt.global.util;

import com.madeyepeople.pocketpt.domain.account.social.AccountPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityUtil {
    public static String getLoginUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails accountPrincipal = (UserDetails) authentication.getPrincipal();
            // TODO: DB 존재하는지 검증까지 해서 return 하기
            return accountPrincipal.getUsername();
        }
        throw new IllegalStateException("User not authenticated");
    }
}
