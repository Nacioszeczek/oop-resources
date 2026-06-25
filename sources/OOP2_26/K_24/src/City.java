import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class City {

    private String capital;
    private int timezone;
    private String lat;
    private String lon;

    public City(String capital, int timezone, String lat, String lon) {
        this.capital = capital;
        this.timezone = timezone;
        this.lat = lat;
        this.lon = lon;
    }



    public int getTimezone() {
        return timezone;
    }

    private static City parseLine(String line) {
        String[] part = line.split(",", 4);
        if(part.length != 4) throw new IllegalArgumentException();

        return new City(part[0], Integer.parseInt(part[1]), part[2].strip(), part[3].strip());
    }

    public static Map<String, City> parseFile(String path) {
        Map<String, City> cities = new HashMap<>();
        try(BufferedReader reader = Files.newBufferedReader(Path.of(path))) {
            reader.readLine();
            String line;
            while((line = reader.readLine()) != null) {
                City city = parseLine(line);
                cities.put(city.capital, city);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cities;
    }

    @Override
    public String toString() {
        return "City{" +
                "capital='" + capital + '\'' +
                ", timezone=" + timezone +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }
}
