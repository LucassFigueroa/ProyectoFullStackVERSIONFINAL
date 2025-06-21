package com.fitlife.reporte.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI reporteOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Reporte")
                        .description("Documentación interactiva del microservicio de Reporte para FitLife SPA.")
                        .version("1.0.0"));
    }
}
