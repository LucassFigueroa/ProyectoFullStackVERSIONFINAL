package com.fitlife.usuario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI usuarioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Usuario")
                        .description("Documentación interactiva del microservicio de Usuario para FitLife SPA.")
                        .version("1.0.0"));
    }
}
