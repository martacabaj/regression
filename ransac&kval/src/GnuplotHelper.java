import java.io.*;

/**
 * Created by Marta on 2014-12-09.
 */
public class GnuplotHelper {
    private static String GNUPLOT_Path = "C:\\Program Files\\gnuplot\\bin\\wgnuplot.exe";
    public static void generateRunsacScript(String filename,  String inliers, String outliers, double[] theta, double perc) {

        try {
            File file = new File(filename);
            System.out.println(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            String gnuplotPlot = "set datafile separator ';'; plot '"
                    + inliers+"' title 'inliers' linecolor 'red',  '"+outliers+"' title 'ouliers' linecolor rgb 'grey',  "+theta[0]+"+"+theta[1]+"*x title 'best fitted line',  " + (theta[0]+perc)+"+"+theta[1]+"*x linecolor rgb 'green' notitle ,  "+ (theta[0]-perc)+"+"+theta[1]+"*x linecolor rgb 'green' notitle;";

            bw.write(gnuplotPlot);

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void generateRunsacScript(String filename,  String inliers, String outliers, double[] theta) {

        try {
            File file = new File(filename);
            System.out.println(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            String gnuplotPlot = "set datafile separator ';'; plot '"
                    + inliers+"' title 'inliers' linecolor 'red',  '"+outliers+"' title 'ouliers' linecolor rgb 'grey',  "+theta[0]+"+"+theta[1]+"*x title 'best fitted line';";

            bw.write(gnuplotPlot);

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void generateKCrossScript(String filename, String data, double[] theta) {
        try {
            File file = new File(filename);
            System.out.println(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            String gnuplotPlot = "set datafile separator ';'; plot '"
                    + data+"' notitle,  "+theta[0]+"+"+theta[1]+"*x title 'best fitted line';";

            bw.write(gnuplotPlot);

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void runGnuplot(final String command) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    String[] call = { GNUPLOT_Path, "--persist", "-e", command };
                    Runtime rt = Runtime.getRuntime();
                    Process proc = rt.exec(call);
                    InputStream stdin = proc.getErrorStream();
                    InputStreamReader isr = new InputStreamReader(stdin);
                    BufferedReader br = new BufferedReader(isr);
                    String line = null;
                    while ((line = br.readLine()) != null)
                        System.err.println("gnuplot:" + line);
                    int exitVal = proc.waitFor();
                    if (exitVal != 0)
                        System.err.println("gnuplot Process exitValue: "
                                + exitVal);
                    proc.getInputStream().close();
                    proc.getOutputStream().close();
                    proc.getErrorStream().close();
                } catch (Exception e) {
                    System.err.println("Fail: " + e);
                }
            }
        };
        new Thread(task, "ServiceThread").start();
    }
}
