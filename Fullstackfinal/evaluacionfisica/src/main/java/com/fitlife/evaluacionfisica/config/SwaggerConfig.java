package com.fitlife.evaluacionfisica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI evaluacionFisicaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Evaluación Física")
                        .description("Documentación interactiva del microservicio de Evaluación Física para FitLife SPA.")
                        .version("1.0.0"));
    }
}
