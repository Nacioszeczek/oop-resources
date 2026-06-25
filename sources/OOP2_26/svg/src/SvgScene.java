import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class SvgScene implements Svg{
    // tablica, korzystając z polimorfizmu, będzie mogła przechowywać obiekty
    // typu Ellipse oraz Polygon
    private Shape[] shapes;
    private int i;  // index pod który wstawić kolejny obiekt

    public void save(String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            BoundingBox bb = boundingBox();
            // na początku znacznik otwierający svg
            String svg = String.format(
                    Locale.ENGLISH,
                    "<svg height=\"%f\" width=\"%f\" xmlns=\"http://www.w3.org/2000/svg\">\n",
                    bb.height(), bb.width()
            );
            writer.write(svg);
            // elementy rysunku
            writer.write(this.toSvg());
            // na końcu znacznik zamykający svg
            svg = "</svg>";
            writer.write(svg);

            writer.close();
            System.out.println("Zapisano do pliku " + fileName);
        } catch (IOException e) {
            System.err.println("Nie udało się otworzyć pliku: " + e.getMessage());
        }
    }

    private BoundingBox boundingBox() {
        double minWidth = 0;
        double minHeight = 0;
        for (Shape poly : shapes) {
            if (poly == null) continue;

            BoundingBox bb = poly.boundingBox();
            double width = bb.x() + bb.width();
            if (width > minWidth) minWidth = width;
            double height = bb.y() + bb.height();
            if (height > minHeight) minHeight = height;
        }

        return new BoundingBox(0, 0, minWidth, minHeight);
    }

    public SvgScene() {
        shapes = new Shape[3];
        i = 0;
    }

    public void addShape(Shape shape) {
        shapes[i] = shape;
        i = (i + 1) % 3; // 0 -> 1 -> 2 -> 0 -> 1 -> ...
    }

    public String toSvg() {
        String svg = "";
        for (Shape p : shapes) {
            if (p == null) continue;

            svg += p.toSvg() + "\n";
        }
        return svg;
    }
}
