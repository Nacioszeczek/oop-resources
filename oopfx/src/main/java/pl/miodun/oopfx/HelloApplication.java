package pl.miodun.oopfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class HelloApplication extends Application {

	@Override
	public void start(@NotNull final Stage stage) throws IOException {
		final var fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
		final var scene = new Scene(fxmlLoader.load(), 320, 240);
		stage.setTitle("Hello!");
		stage.setScene(scene);
		stage.show();
	}

}
