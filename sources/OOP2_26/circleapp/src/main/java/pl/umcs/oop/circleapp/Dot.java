package pl.umcs.oop.circleapp;

import javafx.scene.paint.Color;

import java.util.Locale;

public record Dot(double x, double y, Color color, double radius) {
    public String toMessage() {
        return String.format(Locale.ENGLISH, "%f %f %s %f", x, y, color.toString(), radius);
    }

    public static Dot fromMessage(String message) {
        String[] parts = message.split(" ");
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        Color c = Color.valueOf(parts[2]);
        double r = Double.parseDouble(parts[3]);
        return new Dot(x, y, c, r);
    }
}
