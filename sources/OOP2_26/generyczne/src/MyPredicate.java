import java.util.function.Predicate;

public class MyPredicate<T extends Comparable<T>> implements Predicate<T> {
    private final T min, max;
    public MyPredicate(T min, T max) {
        this.min = min;
        this.max = max;
    }


    @Override
    public boolean test(T t) {
        return t.compareTo(min) > 0 && t.compareTo(max) < 0;
    }
}
