package com.bugwarsBackend.bugwars;

import com.bugwarsBackend.bugwars.service.GameService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;


@Configuration
@SpringBootApplication
public class BugWarsApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(BugWarsApplication.class, args);
//		SpringApplication.run(BugWarsApplication.class, args);
		GameService gameService = applicationContext.getBean(GameService.class);
		gameService.startGame("test");
	}
}

