package com.example.banhangapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaRepositories(basePackages = "com.example.banhangapi.api.repository")
public class BanhangapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanhangapiApplication.class, args);
	}
}
