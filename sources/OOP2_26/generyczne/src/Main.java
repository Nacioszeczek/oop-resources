import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Double> customList = new CustomList<>();
        customList.addLast(0.0);
        customList.addLast(1.0);
        customList.addLast(2.0);
        customList.addLast(3.0);
        customList.addFirst(100.0);

        System.out.println(customList.getFirst());
        System.out.println(customList.getLast());
        System.out.println("usunięto: " + customList.removeFirst());
        System.out.println("usunięto: " + customList.removeLast());
        System.out.println(customList.getFirst());
        System.out.println(customList.getLast());
        System.out.println(customList.size());
        System.out.println(customList.get(2));

        System.out.println("spełniające: " + countPredicate(customList, 0.99, 2.01));
    }

    public static <T extends Comparable<T>> long countPredicate(List<T> list, T min, T max) {
        MyPredicate<T> predicate = new MyPredicate<T>(min, max);
        return list.stream().filter(predicate).count();
    }
}