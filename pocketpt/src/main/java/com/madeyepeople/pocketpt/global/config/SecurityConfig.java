package com.madeyepeople.pocketpt.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.madeyepeople.pocketpt.domain.account.social.PrincipalOauth2UserService;

// springSecurity Config
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    // Security 를 이용한 각종 권한 접근 경로 등 설정
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/chatlogin").permitAll()
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout").permitAll()
                        .logoutSuccessUrl("/")
                )
                .oauth2Login((oauth2Login) -> oauth2Login
                        .loginPage("/chatlogin")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(principalOauth2UserService)
                        )
                )
                .rememberMe(Customizer.withDefaults());
        return http.build();
    }
}