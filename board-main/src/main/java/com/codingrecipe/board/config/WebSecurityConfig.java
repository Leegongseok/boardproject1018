package com.codingrecipe.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login", "/users/**", "/css/**", "/js/**").permitAll() // 로그인 및 회원가입 페이지 접근 허용
                .anyRequest().authenticated() // 그 외 요청은 인증 필요
                .and()
                .formLogin()
                .loginPage("/login") // 커스텀 로그인 페이지 경로
                .defaultSuccessUrl("/board", true) // 로그인 성공 시 이동 경로
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login") // 로그아웃 성공 시 로그인 페이지로 이동
                .permitAll();
        return http.build();
    }

    // Authentication 설정은 메서드로만 사용 (빈으로 등록하지 않음)
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("password")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
    }
}