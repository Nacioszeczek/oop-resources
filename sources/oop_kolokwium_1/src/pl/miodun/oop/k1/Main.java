package pl.miodun.oop.k1;

import java.util.HashMap;
import java.util.List;

public final class Main {

	private Main() {
	}

	public static void main(final String[] args) {
		final var election = new Election();
		election.populate();

		final var votes = election.getFirstTurn().getVotes();
		final var summarizedAll = Vote.summarize(votes);
		final var summarizedVoivodeship = Vote.summarize(votes, List.of("lubelskie"));
		final var summarizedDistrict = Vote.summarize(votes, List.of("lubelskie", "lubelski"));
		final var summarizedMunicipality = Vote.summarize(votes, List.of("lubelskie", "lubelski", "gm. Bychawa"));

		System.out.println("wszystkie: ");
		System.out.println(summarizedAll);
		System.out.println("lubelskie: ");
		System.out.println(summarizedVoivodeship);
		System.out.println("lubelskie, lubelski: ");
		System.out.println(summarizedDistrict);
		System.out.println("lubelskie, lubelski, gm. Bychawa: ");
		System.out.println(summarizedMunicipality);

		final var map = new VoivodeshipMap();
		map.saveToSvg("map.svg");
		System.out.println();

		final var secondTurnVoivodeship = new HashMap<String, Vote>();
		for (final var voivodeship : VoivodeshipMap.getVoivodeships())
			secondTurnVoivodeship.put(voivodeship, election.getSecondTurn().summarize(List.of(voivodeship)));

		for (final var entry : secondTurnVoivodeship.entrySet()) {
			System.out.printf("województwo %s%n", entry.getKey());
			System.out.println(entry.getValue());
		}

		final var selectableMap = new SelectableMap();
		selectableMap.select("mazowieckie");
		selectableMap.select("lubelskie");
		selectableMap.saveToSvg("selectable_map.svg");

		final var voteMap = new VoteMap(election.getSecondTurn(), secondTurnVoivodeship);
		voteMap.saveToSvg("vote_map.svg");
	}

}
