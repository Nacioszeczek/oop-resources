public abstract class Shape implements Svg{
    protected Style style;

    public Shape(Style style) {
        this.style = style;
    }

    public abstract BoundingBox boundingBox();
}
