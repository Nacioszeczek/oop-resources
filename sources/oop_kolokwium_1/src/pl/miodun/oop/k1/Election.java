package pl.miodun.oop.k1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class Election {

	private final List<Candidate> candidates = new ArrayList<>();
	private final ElectionTurn firstTurn = new ElectionTurn(this.candidates);
	private ElectionTurn secondTurn = null;

	private Candidate winner = null;

	public List<Candidate> candidates() {
		return new ArrayList<>(this.candidates);
	}

	public ElectionTurn getFirstTurn() {
		return this.firstTurn;
	}

	public ElectionTurn getSecondTurn() {
		return this.secondTurn;
	}

	public Candidate getWinner() {
		return this.winner;
	}

	public void populate() {
		this.populateCandidates("kandydaci.txt");
		this.firstTurn.populate("1.csv");

		try {
			this.winner = this.firstTurn.winner();
		} catch (final NoWinnerException e) {
			this.secondTurn = new ElectionTurn(this.firstTurn.runoffCandidates());
			this.secondTurn.populate("2.csv");
			try {
				this.winner = this.secondTurn.winner();
			} catch (final NoWinnerException f) {
				throw new RuntimeException("no winner in second turn", f);
			}
		}
	}

	private void populateCandidates(final String fileName) {
		try (final var reader = Files.newBufferedReader(Path.of(fileName), StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				if(line.isBlank()) continue;
				this.candidates.add(new Candidate(line));
			}
		} catch (final IOException e) {
			throw new RuntimeException("failed to populate candidates", e);
		}
	}

}
