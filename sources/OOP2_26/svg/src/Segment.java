public class Segment {
    private Point a, b;

    public Segment perpendicular() {
        // srodek
        double mx = (a.getX() + b.getX()) / 2;
        double my = (a.getY() + b.getY()) / 2;

        // wektor kierunkowy prostopadly
        double dx = -(b.getY() - a.getY());
        double dy = (b.getX() - a.getX());
        // połowa wektora kierunkowego
        double pdx = dx / 2;
        double pdy = dy / 2;

        Point p1 = new Point(mx + pdx, my + pdy);
        Point p2 = new Point(mx - pdx, my - pdy);
        return new Segment(p1, p2);
    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

    public Segment(Point a, Point b) {
        // używamy konstruktora kopiującego Point
        this.a = new Point(a);
        this.b = new Point(b);
    }

    public double length() {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }

    @Override
    public String toString() {
        return "Segment{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }

    public String toSvg() {
        return "<line x1=\"" + a.getX() + "\" y1=\""+ a.getY() + "\"" +
                " x2=\"" + b.getX() + "\" y2=\"" + b.getY() + "\"" +
                " style=\"stroke:red;stroke-width:2\" />";
    }

    public static Segment maxLength(Segment[] segments) {
        if(segments == null || segments.length == 0) return null;

        Segment max = segments[0];
        for(int i = 1; i < segments.length; i++) {
            Segment seg = segments[i];
            if(seg == null) continue;

            if(seg.length() > max.length())
                max = seg;
        }

        return max;
    }
}
