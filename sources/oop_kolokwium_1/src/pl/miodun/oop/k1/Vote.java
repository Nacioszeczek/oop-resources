package pl.miodun.oop.k1;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;

public final class Vote {

	private final Map<Candidate, Long> votesForCandidate = new HashMap<>();
	private final List<String> location = new ArrayList<>(3);

	private long votes = 0L;

	private Vote() {
	}

	public Map<Candidate, Long> getVotes() {
		return new HashMap<>(this.votesForCandidate);
	}

	public long votes() {
		return this.votes;
	}

	public float percentage(final Candidate candidate) {
		return this.votesForCandidate.getOrDefault(candidate, 0L) / (float) this.votes();
	}

	public List<String> getLocation() {
		return new ArrayList<>(this.location);
	}

	public boolean matchesLocation(final List<String> location) {
		if (this.location.isEmpty() || location.isEmpty()) return true;
		if (location.size() > 3)
			throw new IllegalArgumentException("too many locations");

		final var ownMunicipality = this.location.get(2);
		if (location.size() == 3 && !ownMunicipality.equalsIgnoreCase(location.get(2)))
			return false;

		final var ownDistrict = this.location.get(1);
		if (location.size() >= 2 && !ownDistrict.equalsIgnoreCase(location.get(1)))
			return false;

		final var ownVoivodeship = this.location.getFirst();
		return ownVoivodeship.equalsIgnoreCase(location.getFirst());
	}

	private void addVotes(final Candidate candidate, final long amount) {
		this.votesForCandidate.compute(candidate, (c, current) -> current != null ? current + amount : amount);
		this.votes += amount;
	}

	private void setLocation(final String voivodeship, final String district, final String municipality) {
		this.location.clear();
		this.location.add(voivodeship);
		this.location.add(district);
		this.location.add(municipality);
	}

	@Override
	public String toString() {
		final var result = new StringBuilder();
		for (final var candidate : this.votesForCandidate.keySet())
			result
				.append(candidate.name())
				.append(" - ")
				.append(String.format("%.2f", this.percentage(candidate) * 100.0f))
				.append("%\n");
		return result.toString();
	}

	public static Vote fromCsvLine(final List<Candidate> candidates, final String line) {
		final var parts = line.split(",", -1);
		if (parts.length != candidates.size() + 3)
			throw new IllegalArgumentException("candidate count %d does not match line split %d: %s".formatted(
				candidates.size(), parts.length, line));

		final var vote = new Vote();
		vote.setLocation(parts[2], parts[1], parts[0]);

		for (var i = 0; i < candidates.size(); i++) {
			final var candidate = candidates.get(i);
			vote.addVotes(candidate, parseLong(parts[i + 3]));
		}

		return vote;
	}

	public static Vote summarize(final List<Vote> votes) {
		return summarize(votes, null);
	}

	public static Vote summarize(final List<Vote> votes, final List<String> location) {
		final var result = new Vote();
		for (final var vote : filterByLocation(votes, location)) {
			for (final var entry : vote.votesForCandidate.entrySet()) {
				result.addVotes(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	public static List<Vote> filterByLocation(final List<Vote> votes, final List<String> location) {
		if (votes == null || votes.isEmpty()) return Collections.emptyList();
		if (location == null || location.isEmpty()) return new ArrayList<>(votes);
		if (location.size() > 3) throw new IllegalArgumentException("too many locations");

		return votes.stream()
			.filter(v -> v.matchesLocation(location))
			.collect(Collectors.toList());
	}

}
