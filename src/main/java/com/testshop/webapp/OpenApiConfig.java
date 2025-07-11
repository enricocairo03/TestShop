package com.testshop.webapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig
{
    //http://localhost:5051/swagger-ui.html

    @Bean
    public OpenAPI customOpenAPI()
    {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("PRODOTTI WEB SERVICE API")
                        .description("Spring Boot REST API per la gestione articoli TestShop")
                        .termsOfService("terms")
                        .contact(new Contact().email("enrico03.cairo@gmail").name("Enrico Cairo").url("https://xantrix.it/info"))
                        .license(new License().name("Apache License Version 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .version("1.0")
                );
    }
}
