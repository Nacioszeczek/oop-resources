import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PlantUMLRunner {
    static String plantUmlPath;
    public static void setJarPath(String path) {
        plantUmlPath = path;
    }

    public static void generateUml(String data, String dirPath, String fileName) {
        File directory = new File(dirPath);
        directory.mkdirs();
        File outputFile = new File(dirPath + "/" + fileName);
        try {
            FileWriter writer = new FileWriter(outputFile);
            writer.write(data);
            writer.close();
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", plantUmlPath, outputFile.getPath());
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
