package com.bugwarsBackend.bugwars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class BugWarsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BugWarsApplication.class, args);
	}

}
