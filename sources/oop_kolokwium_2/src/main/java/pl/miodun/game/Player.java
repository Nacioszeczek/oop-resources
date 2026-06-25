package pl.miodun.game;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Player {

	private @Nullable Duel duel;

	protected void makeGesture(@NotNull final Gesture gesture) {
		if (this.duel == null) throw new IllegalStateException("No duel");
		this.duel.handleGesture(this, gesture);
	}

	void enterDuel(@NotNull final Duel duel) {
		this.duel = duel;
	}

	void leaveDuel() {
		this.duel = null;
	}

	public boolean isDuelling() {
		return this.duel != null;
	}

}
