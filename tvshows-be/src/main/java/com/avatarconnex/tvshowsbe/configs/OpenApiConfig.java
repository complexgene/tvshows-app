package com.avatarconnex.tvshowsbe.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
  info = @Info(title = "TV Shows API", version = "v1",
               description = "Browse shows and fetch detailed information."),
  servers = { @Server(url = "/v1", description = "Local") }
)
@Configuration
public class OpenApiConfig {}
