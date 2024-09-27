package com.springboot_aws.config.auth;


import com.springboot_aws.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링시큐리티의 설정을 활성화 시켜준다.
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    // bean으로 관리하고 람다식을 통해 함수형으로 설정하게 지향
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; script-src 'self' https://code.jquery.com https://stackpath.bootstrapcdn.com 'nonce-{nonce}'; style-src 'self' https://stackpath.bootstrapcdn.com")))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() // 전체 열람 가능
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name()) // "/api/v1/**"는 USER만 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 /로 이동
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 소셜 로그인 성공 후 추가 기능 명시
                        )
                );

        return http.build();
    }

}