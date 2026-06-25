import java.util.Locale;

public class Ellipse extends Shape{
    private Point center;
    private double rx, ry;

    public Ellipse(Point center, double rx, double ry, Style style) {
        super(style);
        this.center = center;
        this.rx = rx;
        this.ry = ry;
    };

    @Override
    public String toSvg() {
        return String.format(Locale.ENGLISH,
                "<ellipse rx=\"%f\" ry=\"%f\" cx=\"%f\" cy=\"%f\"" +
                " style=\"%s\" />",
                rx, ry, center.getX(), center.getY(), style.toSvg()
        );
    }

    @Override
    public BoundingBox boundingBox() {
        return new BoundingBox(
                center.getX() - rx,
                center.getY() - ry,
                2*rx,
                2*ry
        );
    }
}
