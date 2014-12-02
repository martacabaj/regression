import java.io.*;
import java.util.ArrayList;

/**
 * Created by Marta on 2014-12-02.
 */
public class fileHelper {

    public static void readDataFromFile(DataSet dataSet, int numOfDimension, String filePath) throws IOException {
        File datafile = new File(filePath);
        BufferedReader buffer = new BufferedReader(new FileReader(datafile));
        String line;
        dataSet.setIntervalBegin((line = buffer.readLine()) == null ? null : Double
                .parseDouble(line));
        dataSet.setIntervalEnd((line = buffer.readLine()) == null ? null : Double
                .parseDouble(line));

        while ((line = buffer.readLine()) != null) {
            String[] parts = line.split(";");
            double[] coordinates = new double[numOfDimension];

            for (int i = 0; i < numOfDimension; i++) {
                coordinates[i] = Double.parseDouble(parts[i]);
            }

            dataSet.getSet().add(coordinates);
        }

        buffer.close();
    }
    public static void writePlotScript(String filename, String gnuplotPlot, String trainingPath, String validationPath) {
        Boolean firstLine = false;
        try {
            File file = new File(filename + ".plt");

            if (!file.exists()) {
                file.createNewFile();
                gnuplotPlot =  "set datafile separator ';'; plot '"
                        + trainingPath
                        + "' every::2 pt 7 lc rgb 'grey' title 'training data',  '"
                        +validationPath + "' every::2 pt 3 lc rgb 'red' title 'validation data',  "
                        + gnuplotPlot;
                firstLine = true;
            }
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true), "UTF-8"));

            writer.append(firstLine ? gnuplotPlot : ",  " + gnuplotPlot);

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void writeErrorToFile(String filename, int degree, double value) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true), "UTF-8"));

            writer.append(degree + ";" + value+ '\n');

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    public static void writePlotErrorScript(String filename,  String trainingError, String validationError) {
        Boolean firstLine = false;
        try {
            File file = new File(filename + ".plt");

            if (!file.exists()) {
                file.createNewFile();
            }
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true), "UTF-8"));
            String script ="set datafile separator ';'; plot '"
                    + trainingError
                    + "' pt 7 lc rgb 'grey' title 'training error',  '"
                    +validationError + "' pt 3 lc rgb 'red' title 'validation error',  ";
            writer.append("");

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
