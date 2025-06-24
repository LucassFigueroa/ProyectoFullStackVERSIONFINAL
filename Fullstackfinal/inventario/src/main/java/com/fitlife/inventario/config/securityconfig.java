package com.fitlife.inventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class securityconfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/inventario/**").hasAnyRole("STAFF", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/inventario/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/inventario/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/inventario/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic();
        return http.build();
    }
}
