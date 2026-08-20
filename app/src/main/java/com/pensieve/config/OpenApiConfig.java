package com.pensieve.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI pensieveOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pensieve API")
                        .description("API para criação de gatilhos e revisões espaçadas.")
                        .version("v1"))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor local"));
    }
}
