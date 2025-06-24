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
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Swagger libre
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()

                // GET protegido: ADMIN, SOPORTE, STAFF
                .requestMatchers(HttpMethod.GET, "/pago/**").hasAnyRole("ADMIN", "SOPORTE", "STAFF")

                // POST y PUT: ADMIN, STAFF
                .requestMatchers(HttpMethod.POST, "/pago/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/pago/**").hasAnyRole("ADMIN", "STAFF")

                // DELETE: solo ADMIN
                .requestMatchers(HttpMethod.DELETE, "/pago/**").hasRole("ADMIN")

                // Todo lo demás autenticado
                .anyRequest().authenticated()
            )
            .httpBasic(); // SOLO BASIC AUTH, sin formLogin
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
