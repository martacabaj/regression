import java.util.ArrayList;

/**
 * Created by Marta on 2014-12-06.
 */
public class Regression {
    private static int ITERATIONS = 1000;
    double threshold_val = 0.01;
    private double[] theta;
    private double[] error;
    private int num_of_dimensions = 2;
    private int degree_of_model = 1;
    private double alpha = 0.1;
    private ArrayList<double[]> trainingSet;

    public Regression() {
        theta = new double[degree_of_model + 1];
        error = new double[ITERATIONS];
        theta[0] = 4;
        theta[1] = 3;

    }

    // getters setters
    public ArrayList<double[]> getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(ArrayList<double[]> trainingSet) {
        this.trainingSet = trainingSet;
    }

    public double[] getTheta() {
        return theta;
    }


    public double[] getError() {
        return error;
    }

    public void setError(double[] error) {
        this.error = error;
    }

    private double getH(double[] coordinates) {
        double h = 0;
        h = theta[0];

        for (int i = 1; i < theta.length; i++) {

            h += theta[i] * Math.pow(coordinates[0], i);
        }
        return h;
    }

    public void gradientDescent() {
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
    public double calculateCostFunction(ArrayList<double[]> dataSet) {
        double cost = 0;
        for (int i = 0; i < dataSet.size(); i++) {

            cost += Math.pow(getH(dataSet.get(i)) - dataSet.get(i)[1], 2);

        }
        cost /= (2 * dataSet.size());

        return cost;
    }

    public double calculateMSE(double[] point) {
        double cost = 0;
        cost = Math.pow(getH(point) - point[1], 2);
        return cost;
    }

    private double[] updateTheta() {
        double[] tempTheta = new double[theta.length];
        double sum;
        for (int i = 0; i < tempTheta.length; i++) {
            sum = 0;
            for (int j = 0; j < trainingSet.size(); j++) {
                if (i == 0) {
                    sum += (getH(trainingSet.get(j)) - trainingSet.get(j)[1]);
                } else {
                    sum += (getH(trainingSet.get(j)) - trainingSet.get(j)[1])
                            * Math.pow(trainingSet.get(j)[0], i);
                }
            }
            tempTheta[i] = (theta[i] - (alpha / trainingSet.size()) * sum);

        }
        return tempTheta;
    }
}
