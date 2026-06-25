package pl.miodun.oopfx;

import javafx.application.Application;
import org.jetbrains.annotations.NotNull;

public final class Launcher {

	private Launcher() {
	}

	static void main(@NotNull final String @NotNull [] args) {
		Application.launch(HelloApplication.class, args);
	}

}
