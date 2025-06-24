package com.fitlife.horario.config;

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
                // ✅ Swagger libre para probar sin token
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // ✅ SOLO GET: STAFF, ENTRENADOR y ADMIN pueden ver horarios
                .requestMatchers(HttpMethod.GET, "/horarios/**").hasAnyRole("ADMIN", "ENTRENADOR", "STAFF")

                // ✅ POST, PUT, DELETE: SOLO ADMIN y ENTRENADOR pueden modificar horarios
                .requestMatchers(HttpMethod.POST, "/horarios/**").hasAnyRole("ADMIN", "ENTRENADOR")
                .requestMatchers(HttpMethod.PUT, "/horarios/**").hasAnyRole("ADMIN", "ENTRENADOR")
                .requestMatchers(HttpMethod.DELETE, "/horarios/**").hasAnyRole("ADMIN", "ENTRENADOR")

                // ✅ Todo lo demás requiere estar autenticado (por seguridad)
                .anyRequest().authenticated()
            )
            .httpBasic();

        return http.build();
    }
}
