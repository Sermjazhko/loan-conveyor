package com.dossier.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Dossier",
                description = "Formation of letters and documents. Sending an email to the Client", version = "1.0.0",
                contact = @Contact(
                        name = "Sermyazhko Elizaveta",
                        email = "seldead@mail.ru"
                )
        )
)
public class OpenApiConfig {
}
