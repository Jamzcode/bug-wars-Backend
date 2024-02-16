package com.bugwarsBackend.bugwars;

import com.bugwarsBackend.bugwars.game.Battleground;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@SpringBootApplication
public class BugWarsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BugWarsApplication.class, args);
		Resource mapResource = new ClassPathResource("maps/tunnel.txt");
		Battleground battleground = new Battleground(mapResource);
		battleground.printGrid();
	}

}
