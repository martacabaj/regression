import java.io.*;
import java.util.ArrayList;

/**
 * Created by Marta on 2014-12-06.
 */
public class FileHelper {
public static double fiveperc;
    public static ArrayList<double[]> readFromFile(int numOfDimension, String filePath) throws IOException {
        double maxY = Double.MIN_VALUE, minY = Double.MAX_VALUE;
        ArrayList<double[]> dataSet = new ArrayList<double[]>();
        File datafile = new File(filePath);
        BufferedReader buffer = new BufferedReader(new FileReader(datafile));
        String line;
        line = buffer.readLine();
        line = buffer.readLine();
        while ((line = buffer.readLine()) != null) {
            String[] parts = line.split(";");
            double[] coordinates = new double[numOfDimension];

            for (int i = 0; i < numOfDimension; i++) {
                coordinates[i] = Double.parseDouble(parts[i]);
            }
            minY = minY > coordinates[1] ? coordinates[1] : minY;
            maxY = maxY < coordinates[1] ? coordinates[1] : maxY;

            dataSet.add(coordinates);
        }

        buffer.close();

        fiveperc = (maxY-minY)*5d/100d;
        return dataSet;
    }

    public static void writeSetToFile(String filename, ArrayList<double[]> set) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            String line = "";

            for (int i = 0; i < set.size(); i++) {
                line = set.get(i)[0] + ";" + set.get(i)[1];
                bw.write(line);
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
