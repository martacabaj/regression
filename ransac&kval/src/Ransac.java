import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Marta on 2014-12-06.
 */
public class Ransac {
    private static int NUM_OF_POINTS = 10;

    private final String Home_Path = "C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\task\\";
    private static String inliersFile = "inliers.txt";
    private static String outliersFile = "outliers.txt";
    private static String ransacScript = "ransac.plt";

    private final int MAX_ITERATIONS = 1000;
    private ArrayList<double[]> wholeSet;

    private ArrayList<double[]> trainingSet;
    private ArrayList<double[]> bestInliers;
    private ArrayList<double[]> bestOutliers;
    private double[] theta;
    private double threshold = 0.1d;

    private double smallestMSE = Double.MAX_VALUE;
    private int bestinnum = Integer.MIN_VALUE;
    private double[] bestTheta;
    double fivePerc;
    public Ransac() {

    }
    public Ransac(ArrayList<double[]> wholeSet) {
        this.wholeSet = wholeSet;
        bestInliers = new ArrayList<double[]>();
        bestOutliers = new ArrayList<double[]>();
        fivePerc = FileHelper.fiveperc;

    }

    public int getBestinnum() {
        return bestinnum;
    }

    public double[] getBestTheta() {
        return bestTheta;
    }

    public void randomChoosePoints() {
        Random rand = new Random();

        trainingSet = new ArrayList<double[]>();
        ArrayList<double[]> tempSet = new ArrayList<double[]>(wholeSet);
        for (int i = 0; i < NUM_OF_POINTS; i++) {
            int num = rand.nextInt(tempSet.size());
            trainingSet.add(tempSet.get(num));

            tempSet.remove(num);

        }

    }

    public void run() {
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            randomChoosePoints();
            calculateRegression();
        }
        FileHelper.writeSetToFile(inliersFile, bestInliers);
        FileHelper.writeSetToFile(outliersFile, bestOutliers);
        GnuplotHelper.generateRunsacScript(ransacScript, Home_Path+inliersFile, Home_Path+outliersFile, bestTheta);
        String command = "load '"+Home_Path+ransacScript+"'";

        GnuplotHelper.runGnuplot(command);
    }

    private void calculateRegression() {
        Regression reg = new Regression();
        reg.setTrainingSet(trainingSet);
        reg.gradientDescent();
        double mse = reg.calculateCostFunction(wholeSet);
        theta = reg.getTheta();
        determineType(reg, mse);
    }

    private void determineType(Regression reg, double mse) {
        ArrayList<double[]> inliers = new ArrayList<double[]>();
        ArrayList<double[]> outliers = new ArrayList<double[]>();

        for (double[] point : wholeSet) {

          //  if (ifInliers(point, theta)) {
if(calculateLineDistance(point)<threshold){
                inliers.add(point);
            } else {
                outliers.add(point);
            }
        }

        if (inliers.size()>bestinnum ) {
        //    System.out.println("new mse " + mse);
            bestinnum = inliers.size();

            smallestMSE = mse;

            bestTheta = theta;
            bestInliers.clear();
            bestOutliers.clear();
            bestInliers.addAll(inliers);
            bestOutliers.addAll(outliers);
        }

    }
    private Boolean ifInliers(double[] point, double[]theta){
        double yhigehrBoundary = theta[0]+fivePerc + theta[1]*point[0];
        if(yhigehrBoundary> point[1]){
            double ylowerBoundary = theta[0]-fivePerc + theta[1]*point[0];
            if(ylowerBoundary< point[1]) {
return true;
            }
        }
        return false;
    }
    private double calculateLineDistance(double[] point) {
        double result = Math.abs(theta[0] + theta[1] * point[0] - point[1]) / Math.sqrt(Math.pow(theta[1], 2) + 1);
        return result;
    }

}
