import java.util.Locale;

public class Style {
    // final oznacza, że pole raz ustawione, nie może być modyfikowane
    private final String fillColor, strokeColor;
    private final double strokeWidth;

    public Style(String fillColor, String strokeColor, double strokeWidth) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
    }

    public String toSvg() {
        return String.format(
                Locale.ENGLISH,
                "fill:%s;stroke:%s;stroke-width:%f",
                fillColor, strokeColor, strokeWidth
        );
    }
}
