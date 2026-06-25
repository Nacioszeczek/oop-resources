package pl.miodun.oop.k1;

public final class NoWinnerException extends Exception {

	public NoWinnerException() {
		super("no candidate surpassed 50%");
	}

}
