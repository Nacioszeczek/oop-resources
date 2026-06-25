import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, City> cities = City.parseFile("strefy.csv");

        DigitalClock clock = new DigitalClock(DigitalClock.Type.H24, cities.get("Warszawa"));

        clock.setTime(23, 30, 0);
        System.out.println("Czas: " + clock.toString());
        clock.setCity(cities.get("Kijów"));
        System.out.println("Czas: " + clock.toString());
        clock.setCity(cities.get("Warszawa"));
        System.out.println("Czas: " + clock.toString());


//        System.out.println(cities.toString());
        AnalogClock analog = new AnalogClock(cities.get("Warszawa"));
        analog.toSvg("przyklad.svg");
    }
}