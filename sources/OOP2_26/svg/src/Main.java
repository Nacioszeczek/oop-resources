public class Main {
    public static void main(String[] args) {
        Point p1 = new Point();
        p1.setX(50.0);
        p1.setY(50.0);
        System.out.println(p1);
        System.out.println(p1.toSvg());

        p1.translate(10, -20);
        System.out.println(p1);
        System.out.println(p1.toSvg());

        Point p2 = p1.translated(-30, 40);
        System.out.println(p2);
        System.out.println(p2.toSvg());

        Segment s1 = new Segment(p1, p2);
        System.out.println("długość s1 = " + s1.length());
        System.out.println(s1);
        System.out.println(s1.toSvg());


        Segment[] segments = new Segment[3];
        // (0, 0) -- (0, 40); długość 40
        segments[0] = new Segment(new Point(0, 0), new Point(0, 40));
        // (60.0, 30.0) -- (30.0, 70.0); długość = 50
        segments[1] = s1;
        // (0, 0) -- (44, 0)
        segments[2] = new Segment(new Point(0, 0), new Point(33, 0));

        Segment max = Segment.maxLength(segments);
        System.out.println("najdluzszy: " + max);
        System.out.println();

        System.out.println("s1 przed modyfikacjami: " + s1);
        p1.setX(100);
        p2.setY(100);
        System.out.println("s1 po modyfikacjach: " + s1);
        System.out.println("najdluzszy po modyfikacjach: " + max);


    }
}