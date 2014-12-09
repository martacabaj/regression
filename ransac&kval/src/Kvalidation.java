import java.util.ArrayList;

/**
 * Created by Marta on 2014-12-06.
 */
public class Kvalidation {
    private final String Home_Path = "C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\task\\";

    private static String KScript = "K.plt";
    private static int K_SUBSETS = 10;
    ArrayList<double[]> wholeSet;
    double[] thetaMean;
    double valError;
    double wholeError;


    public Kvalidation(ArrayList<double[]> set) {
        this.wholeSet = set;

    }

    public void run() {
        for (int i = 0; i < K_SUBSETS; i++) {
            divideIntoSets(i);
        }
        valError = calculateMeanError(valError);
        wholeError = calculateMeanError(wholeError);
        calculateMeanTheta();
        GnuplotHelper.generateKCrossScript(KScript, Main.Path, thetaMean);
        GnuplotHelper.runGnuplot("load '"+Home_Path+KScript+"'");

    }

    public void divideIntoSets(int selectedNum) {

        KSubsets(new ArrayList<double[]>(wholeSet), selectedNum);

    }

    private void KSubsets(ArrayList<double[]> dataSet, int setNum) {
        int numInKSet = dataSet.size() / K_SUBSETS;

        ArrayList<double[]> validationSet;
        if (setNum + 1 == K_SUBSETS) {
            validationSet = new ArrayList<double[]>(dataSet.subList(numInKSet * setNum, dataSet.size()));
        } else {
            validationSet = new ArrayList<double[]>(dataSet.subList(numInKSet * setNum, numInKSet * setNum + numInKSet));
        }
        dataSet.removeAll(validationSet);
        ArrayList<double[]> trainingSet = new ArrayList<double[]>(dataSet);

        Regression reg = new Regression();
        reg.setTrainingSet(trainingSet);

        reg.gradientDescent();

        double mseWhole = reg.calculateCostFunction(wholeSet);
        double mseVal = reg.calculateCostFunction(validationSet);
        double[] theta = reg.getTheta();
        addTheta(theta);
        valError += mseVal;
        wholeError += mseWhole;
        System.out.println("w " + mseWhole + "v " + mseVal + " " + theta[0] + " " + theta[1]);
    }

    private void addTheta(double[] theta) {
        if (thetaMean == null) {
            thetaMean = new double[theta.length];
        }
        for (int i = 0; i < theta.length; i++) {
            thetaMean[i] += theta[i];
        }
    }

    private void calculateMeanTheta() {
        for (int i = 0; i < thetaMean.length; i++) {
            thetaMean[i] = thetaMean[i] / K_SUBSETS;
        }
    }

    private double calculateMeanError(double error) {
        return error / K_SUBSETS;
    }

    public double[] getThetaMean() {

        return thetaMean;
    }

    public double getValError() {
        return valError;
    }

    public double getWholeError() {
        return wholeError;
    }
}


