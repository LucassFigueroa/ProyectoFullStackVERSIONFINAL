package com.fitlife.reserva.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)  // Para @PreAuthorize
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // ADMIN: acceso total
        manager.createUser(User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build());

        // ENTRENADOR: gestiona clases y horarios, ve evaluaciones
        manager.createUser(User.withUsername("entrenador")
                .password(encoder.encode("entrenador123"))
                .roles("ENTRENADOR")
                .build());

        // STAFF: gestiona inventario y reservas, no toca usuarios/roles
        manager.createUser(User.withUsername("staff")
                .password(encoder.encode("staff123"))
                .roles("STAFF")
                .build());

        // SOPORTE: modifica reportes, ve pagos (solo ver, NO modificar)
        manager.createUser(User.withUsername("soporte")
                .password(encoder.encode("soporte123"))
                .roles("SOPORTE")
                .build());

        // CLIENTE: reserva clases, ve su cuenta
        manager.createUser(User.withUsername("cliente")
                .password(encoder.encode("cliente123"))
                .roles("CLIENTE")
                .build());

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Swagger abierto
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // Punto extra: permitir acceso público a home, login si quieres
                .anyRequest().authenticated()
            )
            .httpBasic();

        return http.build();
    }
}
