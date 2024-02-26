package com.bugwarsBackend.bugwars;

import com.bugwarsBackend.bugwars.game.Battleground;
import com.bugwarsBackend.bugwars.game.TickSummary;
import com.bugwarsBackend.bugwars.game.setup.BattlegroundFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@SpringBootApplication
public class BugWarsApplication {

	private static final int MAX_TICKS = 100;

	public static void main(String[] args) {
		SpringApplication.run(BugWarsApplication.class, args);
		Resource mapResource = new ClassPathResource("maps/tunnel.txt");
		BattlegroundFactory battlegroundFactory = new BattlegroundFactory(mapResource);
		Battleground battleground = battlegroundFactory.printGrid();

		// Print the initial state
		battleground.print();

		// Simulate ticks and print after each tick
		for (int i = 0; i < MAX_TICKS; i++) {
			TickSummary tickSummary = battleground.nextTick();
			System.out.println("Tick: " + (i + 1));
			battleground.print();
			if (tickSummary.isGameOver()) {
				System.out.println("Game over!");
				break;
			}
		}
	}

}

