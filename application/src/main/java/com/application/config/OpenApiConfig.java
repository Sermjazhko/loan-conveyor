package com.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Application",
                description = "Implementation of the microservice Application (application)", version = "1.0.0",
                contact = @Contact(
                        name = "Sermyazhko Elizaveta",
                        email = "seldead@mail.ru"
                )
        )
)
public class OpenApiConfig {
}
