import java.util.HashMap;
import java.util.Map;

public class Family {
    private Map<String, Person> members;
    public Family() {
        members = new HashMap<>();
    }

    public void add(Person p) {
        members.put(p.getName() + ' ' + p.getSurname(), p);
    }

    public Person get(String fullName) {
        return members.get(fullName);
    }
}
