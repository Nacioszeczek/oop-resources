import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ICDCodeTabularOptimizedForMemory implements ICDCodeTabular {
    @Override
    public String getDescription(String icd10) {
        try {
            Scanner scanner = new Scanner(new File("icd10.txt"));  // otworzyć plik icd10.txt
            for (int i = 0; i < 87; i++) {  // (pominąć 87 linii)
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith(icd10)) {   // znaleźć linijkę zaczynającą się od podanego kodu icd10
                    String[] parts = line.split(" ", 2);    // rozdzielić ją na 2 części: kod oraz opis
                    return parts[1];    // zwrócić opis
                }
            }
            throw new IndexOutOfBoundsException("Nie ma kodu icd10");   // jeżeli nie ma takiego kodu to wyrzucić wyjątek IndexOutOfBoundsException
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
