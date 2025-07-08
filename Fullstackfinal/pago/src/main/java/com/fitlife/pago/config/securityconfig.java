package com.fitlife.pago.config;

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
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // Acceso libre para Swagger/OpenAPI
            .requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html"
            ).permitAll()

            // Acceso GET: ADMIN, SOPORTE, STAFF
            .requestMatchers(HttpMethod.GET, "/pago/**")
                .hasAnyRole("ADMIN", "SOPORTE", "STAFF")

            // Acceso POST y PUT: ADMIN, STAFF
            .requestMatchers(HttpMethod.POST, "/pago/**")
                .hasAnyRole("ADMIN", "STAFF")
            .requestMatchers(HttpMethod.PUT, "/pago/**")
                .hasAnyRole("ADMIN", "STAFF")

            // Acceso DELETE: solo ADMIN
            .requestMatchers(HttpMethod.DELETE, "/pago/**")
                .hasRole("ADMIN")

            // Todo lo demás requiere autenticación
            .anyRequest().authenticated()
        )
            // Autenticación básica (sin login visual)
            .httpBasic();

    return http.build();
}



    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("adminsupremo")
                .password(passwordEncoder().encode("2005"))
                .roles("ADMIN")
                .build(),
            User.withUsername("staffuser")
                .password(passwordEncoder().encode("2005"))
                .roles("STAFF")
                .build(),
            User.withUsername("soporteuser")
                .password(passwordEncoder().encode("2005"))
                .roles("SOPORTE")
                .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
