package com.payments.accounts;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
/*@ComponentScans({ @ComponentScan("com.payments.accounts.controller") })
@EnableJpaRepositories("com.payments.accounts.repository")
@EntityScan("com.payments.accounts.model")*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts microservice REST API Documentation",
				description = "SatyamBank Accounts microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "satya_kaviti",
						email = "satya@gmail.com"

				),
				license = @License(
						name = "Apache 2.0"

				)
		),
		externalDocs = @ExternalDocumentation(
				description =  "SatyamBank Accounts microservice REST API Documentation"
		)
)
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
