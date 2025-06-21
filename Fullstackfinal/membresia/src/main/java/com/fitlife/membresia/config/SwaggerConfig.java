package com.fitlife.membresia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI membresiaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Membresía")
                        .description("Documentación interactiva del microservicio de Membresía para FitLife SPA.")
                        .version("1.0.0"));
    }
}
