import java.io.IOException;

/**
 * Created by Marta on 2014-12-02.
 */
public class regression {

    private static String BASE_PATH = "C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\task4\\";
    private static String TRAINING_PATH = "train4.txt";
    private static String VALIDATION_PATH = "validation4.txt";
    private static String SCRIPT_NAME = "regression";
    private static String SCRIPT_ERROR = "error";
    private static String TRAIN_ERROR_NAME = "trainError4.txt";
    private static String VaLIDATION_ERROR_NAME = "valError4.txt";
    private static int NUM_OF_DIMENSIONS = 2;
    private int DEGREE_OF_MODEL = 10;
    //private static double threshold_val = 0.000001;
    private double alpha = 0.9;
    private static int ITERATIONS = 1000;
    private double desireIntervalBegin = -1;
    private double desireIntervalEnd = 1;
    private double[] theta;
    private double[] error;
    private DataSet trainingSet;
    private DataSet validationSet;

    public static void main(String[] args) {
        regression reg = new regression();
        try {

            reg.init();

            reg.gradientDescent();
            reg.validate();
            reg.plotFunction();
            reg.saveLastError();
            reg.writeErrorsToScript();
        } catch (IOException e) {

            e.printStackTrace();
            System.err.println("Cannot perform regresion. Error occurs. "
                    + e.getMessage());
        }

    }

    // REGRESSION
    public regression() {
        theta = new double[DEGREE_OF_MODEL + 1];
        error = new double[ITERATIONS];
        trainingSet = new DataSet();
        validationSet = new DataSet();
    }

    // getters setters
    public double[] getTheta() {
        return theta;
    }

    public void setTheta(double[] theta) {
        this.theta = theta;
    }

    public double[] getError() {
        return error;
    }

    public void setError(double[] error) {
        this.error = error;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    // read data from file and init theta
    public void init() throws IOException {
        fileHelper.readDataFromFile(trainingSet, NUM_OF_DIMENSIONS, BASE_PATH + TRAINING_PATH);
        fileHelper.readDataFromFile(validationSet, NUM_OF_DIMENSIONS, BASE_PATH + VALIDATION_PATH);
        scaleFeatures(trainingSet);
    }


    private void scaleFeatures(DataSet dataSet) {

        for (int i = 0; i < dataSet.getSet().size(); i++) {

            dataSet.getSet().get(i)[0] = ((desireIntervalEnd - desireIntervalBegin)
                    * (dataSet.getSet().get(i)[0] - dataSet.getIntervalBegin()) / (dataSet.getIntervalEnd() - dataSet.getIntervalBegin()))
                    + desireIntervalBegin;

        }
    }

    // gradient descent

    /**
     * Calculate hypothesis h(theta(x)) = theta0*coordinates[0] +
     * theta1*coordinates[1]+theta2*coordinates[1]^2....+thetaD*coordinates[1]^D
     * (up to defined (D)degree of polynomial)
     * coordinates[0] always set to 1;
     *
     * @param coordinates
     * @return
     */
    private double getH(double[] coordinates) {
        double h = 0;
        h = theta[0];

        for (int i = 1; i < theta.length; i++) {

            h += theta[i] * Math.pow(coordinates[0], i);
        }
        return h;
    }

    private void gradientDescent() {
        for (int i = 0; i < ITERATIONS; i++) {
            error[i] = calculateCostFunction(trainingSet);

            theta = updateTheta();
            // stop when error does not change much
            // in order to represents all data on the same plot, not taken into
            // consideration

            // if (i > 0 && Math.abs(error[i] - error[i - 1]) <= threshold_val)
            // {
            //
            // ITERATIONS = i;
            // break;
            // }
        }
    }

    /**
     * Calculation of cost function which will be minimized J(theta0, theat1)=
     * 1/(2m) sum(h(x)-y(x))^2 (least square error)
     *
     * @return value of cost function for current theta
     */
    private double calculateCostFunction(DataSet dataSet) {
        double cost = 0;
        for (int i = 0; i < dataSet.getSet().size(); i++) {

            cost += Math.pow(getH(dataSet.getSet().get(i)) - dataSet.getSet().get(i)[1], 2);

        }
        cost /= (2 * dataSet.getSet().size());

        return cost;
    }

    private double[] updateTheta() {
        double[] tempTheta = new double[theta.length];
        double sum;
        for (int i = 0; i < tempTheta.length; i++) {
            sum = 0;

            for (int j = 0; j < trainingSet.getSet().size(); j++) {
                if (i == 0) {
                    sum += (getH(trainingSet.getSet().get(j)) - trainingSet.getSet().get(j)[1]);
                } else {
                    sum += (getH(trainingSet.getSet().get(j)) - trainingSet.getSet().get(j)[1])
                            * Math.pow(trainingSet.getSet().get(j)[0], i);

                }
            }
            tempTheta[i] = (theta[i] - (alpha / trainingSet.getSet().size()) * sum);

        }
        return tempTheta;
    }

    private void validate() {
        scaleFeatures(validationSet);
        fileHelper.writeErrorToFile(VaLIDATION_ERROR_NAME, DEGREE_OF_MODEL, calculateCostFunction(validationSet));
    }

    private void saveLastError() {
        fileHelper.writeErrorToFile(TRAIN_ERROR_NAME, DEGREE_OF_MODEL, error[ITERATIONS - 1]);
    }

    private void writeErrorsToScript() {
        fileHelper.writePlotErrorScript(SCRIPT_ERROR, BASE_PATH + TRAIN_ERROR_NAME, BASE_PATH + VaLIDATION_ERROR_NAME);
    }

    private void plotFunction() {

        String function = Double.toString(theta[0]);
        for (int i = 1; i < theta.length; i++) {

            String x = "("
                    + Double.toString(desireIntervalEnd - desireIntervalBegin)
                    + "*(x-" + Double.toString(trainingSet.getIntervalBegin()) + ")/"
                    + Double.toString(trainingSet.getIntervalEnd() - trainingSet.getIntervalBegin()) + ")+"
                    + Double.toString(desireIntervalBegin);
            function += "+" + Double.toString(theta[i]) + "*(" + x + ")**"
                    + Integer.toString(i);
        }

        String plots = function + " lw 2 title 'polynomial degree "
                + DEGREE_OF_MODEL + "'";
        fileHelper.writePlotScript(SCRIPT_NAME, plots, BASE_PATH + TRAINING_PATH, BASE_PATH + VALIDATION_PATH);

    }


}
