package com.ms2transacoes.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final Ms1AuthenticationProvider
            ms1AuthenticationProvider;

    public SecurityConfig(
            Ms1AuthenticationProvider ms1AuthenticationProvider) {

        this.ms1AuthenticationProvider =
                ms1AuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder auth =
                http.getSharedObject(
                        AuthenticationManagerBuilder.class
                );

        auth.authenticationProvider(
                ms1AuthenticationProvider
        );

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(authz -> authz

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .anyRequest()
                        .authenticated()
                )

                .httpBasic(
                        Customizer.withDefaults()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}