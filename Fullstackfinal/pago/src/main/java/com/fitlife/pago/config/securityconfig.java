package com.fitlife.pago.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class securityconfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF, no lo necesitas en REST puro
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); //  Permite todo acceso sin login
        return http.build();
    }
}
