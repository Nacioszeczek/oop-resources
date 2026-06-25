module pl.miodun.oopfx {
	requires javafx.controls;
	requires javafx.fxml;
	requires org.jetbrains.annotations;

	opens pl.miodun.oopfx to javafx.fxml;
	exports pl.miodun.oopfx;
}