import java.util.Arrays;

public class DeathCauseStatistic {
    public class AgeBracketDeaths {
        public final int young;
        public final int old;
        public final int deathCount;


        public AgeBracketDeaths(int young, int old, int deathCount) {
            this.young = young;
            this.old = old;
            this.deathCount = deathCount;
        }

        @Override
        public String toString() {
            return "AgeBracketDeaths{" +
                    "young=" + young +
                    ", old=" + old +
                    ", deathCount=" + deathCount +
                    '}';
        }
    }
    public AgeBracketDeaths Age(Integer age){
        int index = age / 5;
        int young = index * 5;
        int old = young + 4;
        if(age > 95){
            return new AgeBracketDeaths(young, old, death[death.length - 1]);
        }
        return new AgeBracketDeaths(young, old, death[index]);
    }

    private String icd10;
    private int[] death;

    public DeathCauseStatistic(String icd10, int[] death) {
        this.icd10 = icd10;
        this.death = death;
    }

    public static DeathCauseStatistic fromCsvLine(String line){
        String[] value = line.split(",");
        String icd10Code = value[0];
        int[] x = new int[20];
        int index = 0;

        for(int i = 2; i < value.length; i++){
            if(!value[i].equals("-")){
                x[index] = Integer.parseInt(value[i]);
            } else {
                x[index] = 0;
            }
            index++;
        }
        return new DeathCauseStatistic(icd10Code.trim(), x);
    }

    @Override
    public String toString() {
        return "DeathCauseStatistic{" +
                "icd10='" + icd10 + '\'' +
                ", death=" + Arrays.toString(death) +
                '}';
    }

    public String getIcd10() {
        return icd10;
    }
}
