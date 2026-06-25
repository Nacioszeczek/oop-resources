package pl.miodun.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DuelTest {

	@Test
	void duelling() {
		final var playerA = new Player();
		final var playerB = new Player();

		new Duel(playerA, playerB);

		Assertions.assertTrue(playerA.isDuelling());
		Assertions.assertTrue(playerB.isDuelling());
	}

	@Test
	void winner() {
		final var playerA = new Player();
		final var playerB = new Player();
		final var duel = new Duel(playerA, playerB);

		playerA.makeGesture(Gesture.ROCK);
		playerB.makeGesture(Gesture.SCISSORS);

		final var result = duel.evaluate();
		Assertions.assertNotNull(result);
		Assertions.assertEquals(playerA, result.winner());
	}

	@Test
	void draw() {
		final var playerA = new Player();
		final var playerB = new Player();
		final var duel = new Duel(playerA, playerB);

		playerA.makeGesture(Gesture.ROCK);
		playerB.makeGesture(Gesture.ROCK);

		final var result = duel.evaluate();
		Assertions.assertNull(result);
	}

}
