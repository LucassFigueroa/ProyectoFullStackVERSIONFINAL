package com.fitlife.clase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // ✅ Permitir Swagger sin login
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html").permitAll()

                // ✅ TODO lo demás requiere autenticación, y roles ya están en @PreAuthorize
                .anyRequest().authenticated()
            )
            .httpBasic(); // O usa JWT según tu caso
        return http.build();
    }
}
