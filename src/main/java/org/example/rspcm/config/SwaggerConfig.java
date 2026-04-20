package org.example.rspcm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String schemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("RSPCM API")
                        .version("v1")
                        .description("""
                                ### Admin JWT token
                                ```
                                eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkByc3BjbSIsImlhdCI6MTc3NjE4MDkzOSwiZXhwIjo3Mzc3NjE4MDkzO\
                                Swicm9sZXMiOlsiRkFDVE9SX1BBU1NXT1JEIiwiQURNSU4iXX0.x5Tpbj1eLxQpS9fpaDyw76W4ErCZt0zL-CewxojlbTKEymfcogSJ5KaQJzBL7jfE
                                ```
                                """
                ))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .schemaRequirement(
                        schemeName,
                        new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }
}
