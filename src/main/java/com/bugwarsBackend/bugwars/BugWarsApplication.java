package com.bugwarsBackend.bugwars;

import com.bugwarsBackend.bugwars.game.Battleground;
import com.bugwarsBackend.bugwars.game.TickSummary;
import com.bugwarsBackend.bugwars.game.setup.BattlegroundFactory;
import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.repository.ScriptRepository;
import com.bugwarsBackend.bugwars.repository.UserRepository;
import com.bugwarsBackend.bugwars.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@SpringBootApplication
public class BugWarsApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(BugWarsApplication.class, args);
//		SpringApplication.run(BugWarsApplication.class, args);
		GameService gameService = applicationContext.getBean(GameService.class);
		gameService.startGame();
	}

}

