import java.time.LocalDate;

public class NegativeLifespanException extends RuntimeException {
    public NegativeLifespanException(LocalDate birth, LocalDate death) {
        super("Data śmierci wcześniejsza niż data urodzin!: " + birth + " -> " + death);
    }
}
