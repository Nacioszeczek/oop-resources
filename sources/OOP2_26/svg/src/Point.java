public class Point {
    private double x, y;

    // konstruktor to specjalna metoda, której używamy
    // do tworzenia obiektów klasy
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    // konstruktor bezargumentowy
    public Point() {
        this.x = 0;
        this.y = 0;
    }
    // konstruktor kopiujący
    public Point(Point other) {
        this.x = other.x;
        this.y = other.y;
    }

    // akcesor (getter)
    public double getX() {
        return x;
    }
    // mutator (setter)
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + y + ")";
    }

    public String toSvg() {
        return "<circle r=\"2\" cx=\"" + this.x +
                "\" cy=\"" + this.y + "\" fill=\"black\" />";
    }

    public void translate(double dx, double dy) {
        x += dx;
        this.y += dy;
    }

    public Point translated(double dx, double dy) {
        Point newPoint = new Point();
        newPoint.x = this.x + dx;
        newPoint.y = y + dy;
        return newPoint;
    }
}
