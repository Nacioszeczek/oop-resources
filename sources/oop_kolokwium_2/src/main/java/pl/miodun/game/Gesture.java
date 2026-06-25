package pl.miodun.game;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Gesture {
	ROCK, PAPER, SCISSORS;

	public static @Nullable Gesture fromString(@Nullable final String str) {
		if ("r".equalsIgnoreCase(str)) return ROCK;
		if ("p".equalsIgnoreCase(str)) return PAPER;
		if ("s".equalsIgnoreCase(str)) return SCISSORS;
		return null;
	}

	public int compareWith(@NotNull final Gesture other) {
		if (this == other) return 0;
		if (this == PAPER) return other == ROCK ? 1 : -1;
		if (this == ROCK) return other == SCISSORS ? 1 : -1;
		if (this == SCISSORS) return other == PAPER ? 1 : -1;

		throw new RuntimeException("impossible");
	}

}