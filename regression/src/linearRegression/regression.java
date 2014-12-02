package linearRegression;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
/**
 * @author Marta Cabaj 
 * Linear Regression using gradient descent
 * Hypothesis function: 
 * 		h(theta(x)) = theta0 + theta1*x; 
 * Parameters to find:
 * 		theta0, theta1 Cost Function: 1/(2m) sum(h(x)-y(x))^2 (least square error)
 * Goal: 
 * 	minimize cost function J(theta0, theta1)
 */

public class regression {
	// output files
	private static String ERROR_FILE = "error1.txt";
	private static String PARAMETERS_FILE = "parameter1.txt";
	private static String ERROR_PNG = "error1.png";
	private static String REGRESSION_PNG = "regression1.png";
	// input files
	private static String FILE_PATH = "C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\linearRegression\\in1.txt";
	// base paths to gnuplot
	private static String GNUPLOT_Path = "C:\\Program Files\\gnuplot\\bin\\wgnuplot.exe";
	private static String BASE_FILE_PATH = "C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\linearRegression\\";

	private static int NUM_OF_DIMENSIONS = 2;
	private static int DEGREE_OF_MODEL = 1;
	private static double threshold_val = 0.0001;
	private Double intervalBegin, intervalEnd;
	private double alpha = 0.1;
	private static int ITERATIONS = 1000;

	private ArrayList<double[]> dataSet;
	private double[] theta;
	private double[] error;

	public static void main(String[] args) {
		regression reg = new regression();
		try {
			reg.init();
			reg.gradientDescent();
			reg.plotError();
			reg.plotFunction();
			reg.writeThetaToFile();
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
		theta[0] = 4;
		theta[1] = 3;
	}

	/**
	 * Read data from file two first line of file represets interval set first
	 * coordinate of array to 1, next x, at the end y
	 * 
	 * @throws IOException
	 */
	private void readFromFile() throws IOException {
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
			double[] coordinates = new double[NUM_OF_DIMENSIONS + 1];
			coordinates[0] = 1;
			for (int i = 0; i < NUM_OF_DIMENSIONS; i++) {
				coordinates[i + 1] = Double.parseDouble(parts[i]);
			}
			dataSet.add(coordinates);
		}
		buffer.close();
	}

	// gradient descent
	/**
	 * Calculate hypothesis h(theta(x)) = theta0*coordinates[0] +
	 * theta1*coordinates[1].... coordinates[0] always set to 1;
	 * @param coordinates
	 * @return
	 */
	private double getH(double[] coordinates) {
		double h = 0;

		for (int i = 0; i < theta.length; i++) {
			
			h += theta[i] * (i>1? coordinates[1]:coordinates[i]);
		}
		return h;
	}

	private void gradientDescent() {
		for (int i = 0; i < ITERATIONS; i++) {
			error[i] = calculateCostFunction();
			theta = updateTheta();
//			if ((error[i]) <= threshold_val) {
//				ITERATIONS = i;
//				break;
//			}
		}
	}

	/**
	 * Calculation of cost function which will be minimized J(theta0, theat1)=
	 * 1/(2m) sum(h(x)-y(x))^2 (least square error)
	 * @return value of cost function for current theta
	 */
	private double calculateCostFunction() {
		double cost = 0;
		for (int i = 0; i < dataSet.size(); i++) {

			cost += Math.pow(getH(dataSet.get(i))
					- dataSet.get(i)[NUM_OF_DIMENSIONS], 2);

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
				sum += (getH(dataSet.get(j)) - dataSet.get(j)[NUM_OF_DIMENSIONS])
						* (i>1?dataSet.get(j)[1]:dataSet.get(j)[i]);
			}
			tempTheta[i] = theta[i] - (alpha / dataSet.size()) * sum;
		}
		return tempTheta;
	}

	// Write data to file
	private void writeErrorToFile() {
		try {
			File file = new File(ERROR_FILE);
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

	private void writeThetaToFile() {
		try {
			File file = new File(PARAMETERS_FILE);
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
		writeErrorToFile();
		String params = "set datafile separator ';'; set key off;";
		String output = "set term png;set output '" + BASE_FILE_PATH
				+ ERROR_PNG + "';";
		String plot = "plot '" + BASE_FILE_PATH + ERROR_FILE
				+ "' smooth unique";
		String command = params + output + plot;
		executeGnuplot(command);

	}

	private void plotFunction() {
		String function = String.format(Locale.US, "%f +%f*x", theta[0],
				theta[1]);
		String params = "set datafile separator ';'; set key off;";
		String output = "set term png;set output '" + BASE_FILE_PATH
				+ REGRESSION_PNG + "';";
		String plots = "plot '" + FILE_PATH + "' every::2 linecolor rgb 'yellow'; replot " + function
				+ " linecolor rgb 'black' lw 2;";// + output + "replot";
		String command = params + plots;

		executeGnuplot(command);
	}

	private static void executeGnuplot(final String command) {
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
