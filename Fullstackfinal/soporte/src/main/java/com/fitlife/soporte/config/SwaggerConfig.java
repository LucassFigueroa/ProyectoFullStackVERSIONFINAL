package com.fitlife.soporte.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI soporteOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Soporte")
                        .description("Documentación interactiva del microservicio de Soporte para FitLife SPA.")
                        .version("1.0.0"));
    }
}
