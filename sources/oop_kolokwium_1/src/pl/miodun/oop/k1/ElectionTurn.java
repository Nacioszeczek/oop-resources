package pl.miodun.oop.k1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ElectionTurn {

	private final List<Candidate> candidates;
	private final List<Vote> votes = new ArrayList<>();

	public ElectionTurn(final List<Candidate> candidates) {
		this.candidates = candidates; // needs to remain the exact copy
	}

	public List<Candidate> getCandidates() {
		return new ArrayList<>(this.candidates);
	}

	public List<Vote> getVotes() {
		return new ArrayList<>(this.votes);
	}

	public Vote summarize() {
		return this.summarize(null);
	}

	public Vote summarize(final List<String> location) {
		return Vote.summarize(this.votes, location);
	}

	public void populate(final String fileName) {
		try (final var reader = Files.newBufferedReader(Path.of(fileName), StandardCharsets.UTF_8)) {
			reader.readLine(); // skip header
			String line;
			while ((line = reader.readLine()) != null) {
				this.votes.add(Vote.fromCsvLine(this.candidates, line));
			}
		} catch (final IOException e) {
			throw new RuntimeException("unable to populate votes", e);
		}
	}

	public Candidate winner() throws NoWinnerException {
		final var summarized = this.summarize();
		for (final var candidate : this.candidates)
			if (summarized.percentage(candidate) > 0.5f)
				return candidate;

		throw new NoWinnerException();
	}

	public List<Candidate> runoffCandidates() {
		return this.summarize().getVotes().entrySet().stream()
			.sorted((a, b) -> Math.toIntExact(b.getValue() - a.getValue()))
			.limit(2)
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());
	}

}
