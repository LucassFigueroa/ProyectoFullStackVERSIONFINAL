package com.fitlife.notificaciones.config;

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
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // GET: TODOS LOS ROLES
                .requestMatchers(HttpMethod.GET, "/notificaciones/**").permitAll()

                // POST/PUT/DELETE: SOLO ADMIN
                .requestMatchers(HttpMethod.POST, "/notificaciones/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/notificaciones/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/notificaciones/**").hasRole("ADMIN")

                // Todo lo demás autenticado
                .anyRequest().authenticated()
            )
            .httpBasic();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("adminsupremo").password(passwordEncoder().encode("2005")).roles("ADMIN").build(),
            User.withUsername("soporteuser").password(passwordEncoder().encode("2005")).roles("SOPORTE").build(),
            User.withUsername("staffuser").password(passwordEncoder().encode("2005")).roles("STAFF").build(),
            User.withUsername("entrenadoruser").password(passwordEncoder().encode("2005")).roles("ENTRENADOR").build(),
            User.withUsername("clienteuser").password(passwordEncoder().encode("2005")).roles("CLIENTE").build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
