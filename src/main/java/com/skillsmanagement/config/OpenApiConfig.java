package com.skillsmanagement.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


@Configuration
@OpenAPIDefinition(
		info = @Info(title = "Skills Management API", version = "v1", description = "API for managing skills"),
		security = @SecurityRequirement(name = "adminuser")
		)
@SecurityScheme(
		name = "adminuser",
		scheme="basic",
		type = SecuritySchemeType.HTTP,
		in = SecuritySchemeIn.HEADER,
		paramName = "API-KEY"
		)
public class OpenApiConfig {
	@Bean
	public GroupedOpenApi skillsApi() {
		return GroupedOpenApi.builder()
				.group("Skills")
				.pathsToMatch("/v1/**")
				.build();
	}
}
