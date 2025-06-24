package com.fitlife.evaluacionfisica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
    
        var admin = User.withUsername("adminsupremo")
            .password(passwordEncoder().encode("2005"))
            .roles("ADMIN")
            .build();

        var entrenador = User.withUsername("entrenadorsupremo")
            .password(passwordEncoder().encode("2005"))
            .roles("ENTRENADOR")
            .build();

        var staff = User.withUsername("staffsupremo")
            .password(passwordEncoder().encode("2005"))
            .roles("STAFF")
            .build();

       
        var soporte = User.withUsername("soportesupremo")
            .password(passwordEncoder().encode("2005"))
            .roles("SOPORTE")
            .build();

        var cliente = User.withUsername("clientesupremo")
            .password(passwordEncoder().encode("2005"))
            .roles("CLIENTE")
            .build();

        return new InMemoryUserDetailsManager(admin, entrenador, staff, soporte, cliente);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
