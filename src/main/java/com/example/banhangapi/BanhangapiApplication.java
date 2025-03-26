package com.example.banhangapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaRepositories(basePackages = "com.example.banhangapi.api.repository")
public class BanhangapiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		// Kiểm tra biến môi trường
		System.out.println("DB URL: " + dotenv.get("DB_URL"));
		System.out.println("DB Username: " + dotenv.get("DB_USERNAME"));
		System.out.println("DB Password: " + dotenv.get("DB_PASSWORD"));
		System.out.println("JWT Secret: " + dotenv.get("SECRET_KEY"));
		System.out.println("Application Name: " + dotenv.get("APP_NAME"));
		SpringApplication.run(BanhangapiApplication.class, args);
	}
}
