package pl.miodun.oop.k1;

import java.util.Map;

public final class VoteMap extends VoivodeshipMap {

	private static final int COLOR_1 = 0x0000FF;
	private static final int COLOR_2 = 0xFF7300;
	private static final int COLOR_INDETERMINATE = 0;

	public VoteMap(final ElectionTurn turn, final Map<String, Vote> results) {
		final var candidates = turn.getCandidates();
		if (candidates.size() != 2)
			throw new IllegalArgumentException("not second turn");

		final var candidate1 = candidates.getFirst();
		final var candidate2 = candidates.getLast();

		for (final var result : results.entrySet()) {
			final var voivodeship = result.getKey();
			final var vote = result.getValue();

			final var color = vote.percentage(candidate1) > 0.5f
				? COLOR_1
				: vote.percentage(candidate2) > 0.5f
				  ? COLOR_2
				  : COLOR_INDETERMINATE;
			this.setColor(voivodeship, color);
		}
	}

}
