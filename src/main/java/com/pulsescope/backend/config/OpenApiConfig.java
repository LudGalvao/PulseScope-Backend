package com.pulsescope.backend.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pulseScopeOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("PulseScope Core API")
                        .description("API principal para monitoramento inteligente de reputacao digital. "
                                + "Use esta documentacao para testar buscas, mencoes, alertas e indicadores do dashboard.")
                        .version("v1")
                        .contact(new Contact()
                                .name("PulseScope")
                                .email("team@pulsescope.local"))
                        .license(new License()
                                .name("Academic Use")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Ambiente local")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentacao oficial da GNews")
                        .url("https://docs.gnews.io/"));
    }
}
