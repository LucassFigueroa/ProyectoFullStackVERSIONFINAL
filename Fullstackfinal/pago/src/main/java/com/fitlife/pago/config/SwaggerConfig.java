package com.fitlife.pago.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI pagoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Pago")
                        .description("Documentación interactiva del microservicio de Pago para FitLife SPA.")
                        .version("1.0.0"));
    }
}
