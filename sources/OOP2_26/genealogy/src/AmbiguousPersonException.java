public class AmbiguousPersonException extends RuntimeException {

    public AmbiguousPersonException(Person existing) {
        super("Person " + existing.getName() + " " + existing.getSurname() + " already exists.");
    }

}
