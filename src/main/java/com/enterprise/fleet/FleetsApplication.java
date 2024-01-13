package com.enterprise.fleet;

import com.enterprise.fleet.auth.AuthenticationService;
import com.enterprise.fleet.auth.dto.AuthenticationRequestDTO;
import com.enterprise.fleet.auth.dto.RegisterRequestDTO;
import com.enterprise.fleet.user.types.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class FleetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FleetsApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {};
	}
}
