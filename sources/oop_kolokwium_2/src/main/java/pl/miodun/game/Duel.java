package pl.miodun.game;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Duel {

	private final @NotNull Player playerA;
	private final @NotNull Player playerB;

	private @Nullable Gesture gestureA;
	private @Nullable Gesture gestureB;

	private @Nullable Runnable onEnd;

	public Duel(@NotNull final Player playerA, @NotNull final Player playerB) {
		this.playerA = playerA;
		this.playerB = playerB;

		//noinspection ThisEscapedInObjectConstruction
		this.playerA.enterDuel(this);
		//noinspection ThisEscapedInObjectConstruction
		this.playerB.enterDuel(this);
	}

	public void handleGesture(final Player player, final Gesture gesture) {
		if (player == this.playerA) this.gestureA = gesture;
		else if (player == this.playerB) this.gestureB = gesture;
		else throw new IllegalArgumentException("invalid player");

		if(this.canEvaluate() && this.onEnd != null) this.onEnd.run();
	}

	private boolean canEvaluate() {
		return this.gestureA != null && this.gestureB != null;
	}

	public Result evaluate() {
		if (this.gestureA == null || this.gestureB == null) throw new IllegalStateException("not finished");
		this.playerA.leaveDuel();
		this.playerB.leaveDuel();
		final var result = this.gestureA.compareWith(this.gestureB);

		if (result > 0) return new Result(this.playerA, this.playerB);
		else if (result < 0) return new Result(this.playerB, this.playerA);
		else return null;
	}

	public void onEnd(@Nullable final Runnable onEnd) {
		this.onEnd = onEnd;
	}

	public record Result(@NotNull Player winner, @NotNull Player loser) {

	}


}
