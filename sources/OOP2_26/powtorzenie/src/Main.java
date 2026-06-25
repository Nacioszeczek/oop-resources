
public class Main {
    public static void main(String[] args) {
//        DeathCauseStatistic test = DeathCauseStatistic.fromCsvLine("A04.7          ,758,-,-,-,-,-,1,-,1,3,5,9,12,30,58,64,94,161,192,95,33");
//        System.out.println(test);
//        System.out.println(test.Age(0));

        DeathCauseStatisticList statisticList = new DeathCauseStatisticList();
        statisticList.repopulate("zgony.csv");
//        System.out.println(statisticList);
        ICDCodeTabularOptimizedForMemory tabular = new ICDCodeTabularOptimizedForMemory();
        for (DeathCauseStatistic stat : statisticList.mostDeadlyDiseases(0, 4)){
            System.out.println(tabular.getDescription(stat.getIcd10()));
            System.out.println(stat);
        }
    }
}