package polynomialRegression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


/**
 * Regression with one feature. Feature are scaled to the range [1, 1]
 * 
 * @author Marta Cabaj
 * 
 */
public class regression {

	// input files

	public static String setNum = "2";
	private static String REGRESSION_FILE_PLT = "regression2";
	private static String ERROR_FILE_PLT = "error2";
	public static String THETA_BASE = "parameters";
	private static String FILE_PATH = "C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\polynomialRegression\\in2.txt";
	// base paths to gnuplot
	//private static String GNUPLOT_Path = "C:\\Program Files\\gnuplot\\bin\\wgnuplot.exe";
	private static String BASE_FILE_PATH = "C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\polynomialRegression\\";

	private static int NUM_OF_DIMENSIONS = 2;
	private int DEGREE_OF_MODEL = 12;
	//private static double threshold_val = 0.000001;
	private Double intervalBegin, intervalEnd;
	private double alpha = 0.9;
	private static int ITERATIONS = 1000;
	private double desireIntervalBegin = -1;
	private double desireIntervalEnd = 1;
	private ArrayList<double[]> dataSet;
	private double[] theta;
	private double[] error;

	double max, min;

	public static void main(String[] args) {
		regression reg = new regression();
		try {

			reg.init();

			reg.gradientDescent();
			reg.plotError();
			reg.plotFunction();
			String thetaFile = THETA_BASE + setNum
					+ reg.DEGREE_OF_MODEL + ".txt";
			reg.writeThetaToFile(thetaFile);

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
		readFromFile();
		scaleFeatures();
	}

	/**
	 * Read data from file two first line of file represents interval 
	 * 
	 * @throws IOException
	 */
	private void readFromFile() throws IOException {

		max = Double.MIN_VALUE;
		min = Double.MAX_VALUE;
		dataSet = new ArrayList<double[]>();
		File datafile = new File(FILE_PATH);
		BufferedReader buffer = new BufferedReader(new FileReader(datafile));
		String line;
		intervalBegin = (line = buffer.readLine()) == null ? null : Double
				.parseDouble(line);
		intervalEnd = (line = buffer.readLine()) == null ? null : Double
				.parseDouble(line);
		
		while ((line = buffer.readLine()) != null) {
			String[] parts = line.split(";");
			double[] coordinates = new double[NUM_OF_DIMENSIONS];

			for (int i = 0; i < NUM_OF_DIMENSIONS; i++) {
				coordinates[i] = Double.parseDouble(parts[i]);
			}
			if (coordinates[0] > max) {
				max = coordinates[0];
			}
			if (coordinates[0] < min) {
				min = coordinates[0];
			}

			dataSet.add(coordinates);
		}

		buffer.close();
	}

	private void scaleFeatures() {

		for (int i = 0; i < dataSet.size(); i++) {

			dataSet.get(i)[0] = ((desireIntervalEnd - desireIntervalBegin)
					* (dataSet.get(i)[0] - min) / (max - min))
					+ desireIntervalBegin;

		}
	}

	// gradient descent
	/**
	 * Calculate hypothesis h(theta(x)) = theta0*coordinates[0] +
	 * theta1*coordinates[1]+theta2*coordinates[1]^2....+thetaD*coordinates[1]^D
	 * (up to defined (D)degree of polynomial)
	 *  coordinates[0] always set to 1;
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
			error[i] = calculateCostFunction();

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
	private double calculateCostFunction() {
		double cost = 0;
		for (int i = 0; i < dataSet.size(); i++) {

			cost += Math.pow(getH(dataSet.get(i)) - dataSet.get(i)[1], 2);

		}
		cost /= (2 * dataSet.size());

		return cost;
	}

	private double[] updateTheta() {
		double[] tempTheta = new double[theta.length];
		double sum;
		for (int i = 0; i < tempTheta.length; i++) {
			sum = 0;

			for (int j = 0; j < dataSet.size(); j++) {
				if (i == 0) {
					sum += (getH(dataSet.get(j)) - dataSet.get(j)[1]);
				} else {
					sum += (getH(dataSet.get(j)) - dataSet.get(j)[1])
							* Math.pow(dataSet.get(j)[0], i);

				}
			}
			tempTheta[i] = (theta[i] - (alpha / dataSet.size()) * sum);

		}
		return tempTheta;
	}

	// Write data to file
	private void writeErrorToFile(String filename) {
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			String line = "";

			for (int i = 0; i < ITERATIONS; i++) {
				line = i + ";" + getError()[i];
				bw.write(line);
				bw.newLine();
			}
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	private void writeThetaToFile(String filename) {
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			String line = "";

			for (int i = 0; i < theta.length; i++) {
				line = Double.toString(theta[i]);
				bw.write(line);
				bw.newLine();
			}
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}



	// plot using gnuplot
	private void plotError() {
		String filename = "error" + setNum + DEGREE_OF_MODEL + ".txt";
		writeErrorToFile(filename);
		String plot = "'" + BASE_FILE_PATH + filename
				+ "' smooth unique title 'polynomial degree " + DEGREE_OF_MODEL+"'";
		writeScript(ERROR_FILE_PLT, plot);

	}

	private void plotFunction() {
		
		String function = Double.toString(theta[0]);
		for (int i = 1; i < theta.length; i++) {

			String x = "("
					+ Double.toString(desireIntervalEnd - desireIntervalBegin)
					+ "*(x-" + Double.toString(min) + ")/"
					+ Double.toString(max - min) + ")+"
					+ Double.toString(desireIntervalBegin);
			function += "+" + Double.toString(theta[i]) + "*(" + x + ")**"
					+ Integer.toString(i);
		}

		String plots = function + " lw 2 title 'polynomial degree "
				+ DEGREE_OF_MODEL+"'";
		writeScript(REGRESSION_FILE_PLT, plots);

	}

	private void writeScript(String filename, String gnuplotPlot) {
		Boolean firstLine = false;
		try {
			File file = new File(filename + ".plt");
			System.out.println(filename + ".plt");
			if (!file.exists()) {
				file.createNewFile();
				gnuplotPlot = filename == REGRESSION_FILE_PLT ? "set datafile separator ';'; plot '"
						+ FILE_PATH
						+ "' every::2 pt 7 lc rgb 'grey' title 'training data',  "
						+ gnuplotPlot
						: "plot " + gnuplotPlot;
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

}
