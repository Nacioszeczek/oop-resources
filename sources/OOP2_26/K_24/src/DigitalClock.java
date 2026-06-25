public class DigitalClock extends Clock {
    public enum Type {
        H24, H12;
    }

    private Type clockType;
    public DigitalClock(Type clockType, City city) {
        super(city);
        this.clockType = clockType;
    }

    @Override
    public String toString() {
        if (clockType == Type.H24) {
            return super.toString();

        } else {
            if (hours == 0) {
                return String.format("%d:%02d:%02d AM", 12, min, sec);
            }

            if (hours == 12) {
                return String.format("%d:%02d:%02d PM", 12, min, sec);
            }
            if (hours > 12 && hours < 24) {
                return String.format("%d:%02d:%02d PM", hours - 12, min, sec);
            }
            else {
                return String.format("%d:%02d:%02d AM", hours, min, sec);
            }
        }
    }
}
