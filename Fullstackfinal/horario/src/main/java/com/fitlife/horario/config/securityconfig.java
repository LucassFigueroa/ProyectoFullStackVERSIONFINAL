package com.fitlife.horario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class securityconfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Swagger abierto
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
                // GET libre
                .requestMatchers(HttpMethod.GET, "/api/horario/**").permitAll()
                // POST/PUT/DELETE protegido por rol HORARIO
                .requestMatchers(HttpMethod.POST, "/api/horario/**").hasRole("HORARIO")
                .requestMatchers(HttpMethod.PUT, "/api/horario/**").hasRole("HORARIO")
                .requestMatchers(HttpMethod.DELETE, "/api/horario/**").hasRole("HORARIO")
                // Todo lo demás autenticado
                .anyRequest().authenticated()
            )
            .httpBasic(); // SOLO Basic Auth, sin formLogin
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.withUsername("adminsupremo")
            .password(passwordEncoder().encode("2005"))
            .roles("HORARIO")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
