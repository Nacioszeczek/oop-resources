import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DeathCauseStatisticList {
    List<DeathCauseStatistic> statisticList = new ArrayList<>();

    public void repopulate(String filePath) {
        try {
            Scanner scanner = new Scanner(new File(filePath));
            scanner.nextLine();
            scanner.nextLine();
            statisticList.clear();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                DeathCauseStatistic stat = DeathCauseStatistic.fromCsvLine(line);
                statisticList.add(stat);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DeathCauseStatistic> mostDeadlyDiseases(int wiek, int n) {
        List<DeathCauseStatistic> result = new ArrayList<>(statisticList);

        return result.stream().sorted((s1, s2) -> {
            int death = s1.Age(wiek).deathCount;
            int death2 = s2.Age(wiek).deathCount;
            return Integer.compare(death2, death);
        }).limit(n).toList();
    }

    @Override
    public String toString() {
        return "statisticList=" + statisticList;
    }
}
