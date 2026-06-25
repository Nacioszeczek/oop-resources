import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class Person implements Comparable<Person>, Serializable {
    private Set<Person> children;
    private String name;
    private String surname;
    private LocalDate date;
    private LocalDate deth;

    public static Person oldestPerson(List<Person> personList) {
        return personList.stream()
                .filter((Person p) -> p.deth == null)
                .min((Person p1, Person p2) -> {
                    if(p1.date.isAfter(p2.date)) {
                        return 1;
                    } else if(p1.date.isBefore(p2.date)) {
                        return -1;
                    } else {
                        return 0;
                    }
                })
                .get();
    }

    public static List<Person> DeathList(List<Person> personList){
        return personList.stream().filter(person -> person.deth != null)
                .sorted((p1, p2) -> {
                    long d1 = ChronoUnit.DAYS.between(p1.deth, p1.date);
                    long d2 = ChronoUnit.DAYS.between(p2.deth, p2.date);

                    return (int) (d1 - d2);
                }).toList();
    }

    public static List<Person> sortedByBirth(List<Person> personList) {
        return personList.stream()
                .sorted((p1, p2) -> {
                    if(p1.date.isAfter(p2.date)) {
                        return 1;
                    } else if (p1.date.isBefore(p2.date)) {
                        return -1;
                    } else {
                        return 0;
                    }
                })
                .toList();
    }

    public static List<Person> filterSubstring(List<Person> personList, String substring) {
//        List<Person> result = new ArrayList<>();
//        for (Person p : personList) {
//            String name = p.name + " " + p.surname;
//            if (name.contains(substring)) {
//                result.add(p);
//            }
//        }
//        return result;
        return personList.stream()
                .filter((p) -> {
                    String name = p.name + " " + p.surname;
                    return name.contains(substring);
                })
                .toList();
    }

    public static String listToPlantUml(List<Person> personList, Function<String,
                                        String> postProcess, Predicate<Person> condition) {
        String uml = "@startuml\n";
        for (Person p : personList) {
            String line = "object " + p.name + "_" + p.surname;
            if (condition.test(p)) {
                line = postProcess.apply(line);
            }
            uml += line + "\n";
        }
        for (Person p : personList) {
            for (Person child : p.children) {
                uml += child.name + "_" + child.surname + " --> " + p.name + "_" + p.surname + "\n";
            }
        }
        uml += "@enduml";
        return uml;
    }

    public List<Person> getChildren(){
//        List<Person> resultList = new ArrayList<>();
//        for(Person p : children){
//            resultList.add(p);
//        }
//        resultList.sort(Person::compareTo);
//        return resultList;
        return children.stream().sorted().toList();
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Person(String name, String surname, LocalDate date, LocalDate deth){
        this.name = name;
        this.surname = surname;
        this.date = date;
        this.children = new HashSet<>();
        this.deth = deth;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", date=" + date +
                ", deth=" + deth +
                '}';
    }

    public boolean adopt(Person child) {
        boolean success = this.children.add(child);
        return success;
    }

    public Person getYoungestChild() {
        Person result = null;
        for(Person p : this.children) {
            if (result == null || result.compareTo(p) < 0) {
                result = p;
            }
        }
        return result;
    }

    @Override
    public int compareTo(Person other) {
        if (this.date.getYear() == other.date.getYear()) {
            return this.date.getDayOfYear() - other.date.getDayOfYear();
        }
        return this.date.getYear() - other.date.getYear();
    }

    public static Person fromCsvLine(String line) {
        String[] elements = line.split(",", -1);
        String[] name = elements[0].split(" ", 2);
        LocalDate birth = LocalDate.parse(elements[1], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        LocalDate deth = null;
        if (!elements[2].isEmpty()) {
            deth = LocalDate.parse(elements[2], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            if (deth.isBefore(birth)) {
                throw new NegativeLifespanException(birth, deth);
            }
        }
        Person created = new Person(name[0], name[1], birth, deth);
        return  created;
    }

    public static List<Person> fromCsv(String filePath) {
        List<Person> personList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // tutaj wczyta się linia nagłówka, możemy zignorować
            // reader.readLine() zwróci null gdy plik się skończy
            while ((line = reader.readLine()) != null) {
                //System.out.println("wczytana linia: " + line);
                Person parsed = fromCsvLine(line);
                for(Person existing : personList) {
                    if(existing.getName().equals(parsed.getName()) &&
                       existing.getSurname().equals(parsed.getSurname())) {
                        throw new AmbiguousPersonException(existing);
                    }
                }

                String[] elements = line.split(",", -1);
                String parent1 = elements[3];
                String parent2 = elements[4];
                for(Person p : personList) {
                    String fullName = p.name + " " + p.surname;
                    if(fullName.equals(parent1)) {
                        p.adopt(parsed);
                    }

                    if(fullName.equals(parent2)) {
                        p.adopt(parsed);
                    }
                }
                personList.add(parsed);
            }
        } catch (IOException e) {
            System.out.println("Błąd odczytu: " + e.getMessage());
        }
        return personList;
    }

    public static void toBinaryFile(String filePath, List<Person> toSave) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(toSave);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Person> fromBinaryFile(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            List<Person> loaded = (List<Person>)ois.readObject();
            return loaded;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public LocalDate getDeth() {
        return deth;
    }
}
