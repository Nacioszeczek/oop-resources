public class Main2 {
    public static void main(String[] args) {
        // wielokąty -----------------
        Point[] vertices = new Point[4];
        Point v1 = new Point(10, 10);
        vertices[0] = v1;
        vertices[1] = new Point(70, 10);
        Point v3 = new Point(50, 50);
        vertices[2] = v3;
        vertices[3] = new Point(10, 70);

        Polygon poly = new Polygon(vertices);
        System.out.println(poly);
        System.out.println(poly.toSvg());
        vertices[1] = new Point(100, 100);
        v1.setX(100);
        System.out.println("Po zmianie: " + poly);
        System.out.println();

        // SVG scene
        SvgScene scene = new SvgScene();
        scene.addShape(poly);
        scene.addShape(new Polygon(new Point[] {
                new Point(120, 120),
                new Point(160, 160),
                new Point(120, 160)
        }));
        Style myStyle = new Style("red", "green", 3);
        scene.addShape(new Polygon(new Point[] {
                new Point(10, 100),
                new Point(20, 200),
                new Point(80, 160)
        }, myStyle));

        Polygon square = Polygon.square(
                new Segment(
                        new Point(10, 10),
                        new Point(80, 80)
                ), myStyle
        );
        scene.addShape(square);
        Ellipse ellipse = new Ellipse(new Point(120, 120), 20, 50, myStyle);
        scene.addShape(ellipse);

        System.out.println(scene.toSvg());
        scene.save("obrazek.svg");
    }
}
