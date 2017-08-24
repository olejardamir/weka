package RR.prediction.evaluation;

/**
 * This class is created to evaluate the SVM model based on an inputed CSV file.
 * Should there be any error, the integer divided by zero is returned by each method, indicating that something is awfully wrong.
 * <p>
 * @author      Damir Olejar
 * @version     1.0
 * @version		WEKA 3.7
 */


import java.util.ArrayList;

import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.LibSVM;
import RR.prediction.classifiers.SVM.SVM;

public class EvaluateModelSVM {

	private SVM svm = new SVM();
	private Evaluation eval;
	
	
 
	/**
	 * Constructor to load the CSV and the SVM model.
	 * 
	 * @param testCSV Path to a CSV to test.
	 * @param filename Path to a model to load.
	 * 
	 * */
	public EvaluateModelSVM(String testCSV, String filename) {
		try {
			svm.loadModelFromFile(filename); 
			svm.loadCSV(testCSV);
			eval = new Evaluation(svm.getInstances());
			eval.evaluateModel(svm.getModel(), svm.getInstances());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Constructor to load the CSV and the SVM model.
	 * 
	 * @param testCSV Path to a CSV to test.
	 * @param model the LibSVM model to test with testCSV
	 * 
	 * */
	public EvaluateModelSVM(String testCSV, LibSVM model) {
		try {
			svm.setModel(model);
			svm.loadCSV(testCSV);
			eval = new Evaluation(svm.getInstances());
			eval.evaluateModel(svm.getModel(), svm.getInstances());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the summary of a model evaluation
	 * 
	 * @return String summary
	 */
	public String toSummaryString(){
		return eval.toSummaryString(false);
	}
	
	/**
	 * Returns the maximum error of predicted VS actual values
	 * 
	 * @return double Maximum error.
	 */
	public double getMaximumError(){
		double maxerror = 0;
		ArrayList<Prediction> predictions = eval.predictions();
		for (int t=0;t<predictions.size();t++){
			Prediction p = predictions.get(t);
			double error = Math.abs(p.actual()-p.predicted());
			if (maxerror<error){maxerror=error;}
		}
		
		return maxerror;
	}

	
	/**
	 * Returns the Standard Error of Regression, of predicted VS actual values.
	 * The best value is the 0 (smaller is better). It must be <= 2.5 to produce a sufficiently narrow 95% prediction interval.
	 * 
	 * @return double Standard Error of Regression.
	 */	
 
	public double getStandardErrorOfRegression(){
		ArrayList<Prediction> predictions = eval.predictions();
		//Calculate the sum of squares for error (SSE).
		double SSE = 0;
		int cnt=0;
		for (int t=0;t<predictions.size();t++){
			Prediction p = predictions.get(t);
			double predicted = p.predicted();
			double actual = p.actual();
			double calc = Math.pow((predicted-actual), 2);
			if (!(calc+"").equals("NaN")){
			SSE = SSE + calc;
			cnt++;
			}
		}
		//Divide this number by n - 2, where n is the number of data points in the sample.
		double div = SSE/(cnt-2);
		//Find the square root of this value.		
		double sqrt = Math.sqrt(div);
		return sqrt;
	}

	 
	/**
	 * Returns the Standard Error of Regression, of predicted VS actual values.
	 * The best value is the 0 (smaller is better). It must be <= 2.5 to produce a sufficiently narrow 95% prediction interval.
	 * 
	 * @return double Standard Error of Regression.
	 */		
	public double getCorrelationCoefficient(){
		try {
			
			return eval.correlationCoefficient();
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	/**
	 * Returns the Average Cost.
	 * 
	 * @return double Average Cost.
	 */		
	public double getAverageCost(){
		try {
			return eval.avgCost();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	
	/**
	 * Returns the Average Cost.
	 * 
	 * @return double Average Cost.
	 */	
	public double getCoverageOfTestCasesByPredictedRegions(){
		try {
			return eval.coverageOfTestCasesByPredictedRegions();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	
	/**
	 * Returns the Average Cost.
	 * 
	 * @return double Average Cost.
	 */		
	public double getErrorRate(){
		try {
			return eval.errorRate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	/**
	 * Returns the Mean Absolute Error.
	 * 
	 * @return double Mean Absolute Error.
	 */		
	public double getMeanAbsoluteError(){
		try {
			return eval.meanAbsoluteError();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	
	/**
	 * Returns the Prior Absolute Error.
	 * 
	 * @return double Prior Absolute Error.
	 */		
	public double getMeanPriorAbsoluteError(){
		try {
			return eval.meanPriorAbsoluteError();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	
	/**
	 * Returns the Number of Instances (rows) of the loaded CSV file.
	 * 
	 * @return double Number of Instances (rows) of the loaded CSV file.
	 */		
	public double getNumberOfInstances(){
		try {
			return eval.numInstances();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	/**
	 * Returns the Relative Absolute Error.
	 * 
	 * @return double Relative Absolute Error.
	 */		
	public double getRelativeAbsoluteError(){
		try {
			return eval.relativeAbsoluteError();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	/**
	 * Returns the Root Mean Prior Squared Error.
	 * 
	 * @return double Root Mean Prior Squared Error.
	 */		
	public double getRootMeanPriorSquaredError(){
		try {
			return eval.rootMeanPriorSquaredError();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	
	/**
	 * Returns the Root Mean Squared Error.
	 * 
	 * @return double Root Mean Squared Error.
	 */	
	public double getRootMeanSquaredError(){
		try {
			return eval.rootMeanSquaredError();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	
	/**
	 * Returns the Root Mean Prior Squared Error.
	 * 
	 * @return double Root Mean Prior Squared Error.
	 */	
	public double getRootRelativeSquaredError(){
		try {
			return eval.rootRelativeSquaredError();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	
	/**
	 * Returns the SF Entropy Gain.
	 * 
	 * @return double SF Entropy Gain.
	 */		
	public double getSFEntropyGain(){
		try {
			return eval.SFEntropyGain();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	
	/**
	 * Returns the SF Mean Entropy Gain.
	 * 
	 * @return double SF Mean Entropy Gain.
	 */	
	public double getSFMeanEntropyGain(){
		try {
			return eval.SFMeanEntropyGain();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	/**
	 * Returns the SF Mean Prior Entropy Gain.
	 * 
	 * @return double SF Mean Prior Entropy Gain.
	 */		
	public double getSFMeanPriorEntropy(){
		try {
			return eval.SFMeanPriorEntropy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	/**
	 * Returns the SF Mean Scheme Entropy.
	 * 
	 * @return double SF Mean Scheme Entropy.
	 */		
	public double getSFMeanSchemeEntropy(){
		try {
			return eval.SFMeanSchemeEntropy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	
	/**
	 * Returns the SF Prior Entropy.
	 * 
	 * @return double SF Prior Entropy.
	 */	
	public double getSFPriorEntropy(){
		try {
			return eval.SFPriorEntropy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	/**
	 * Returns the SF Scheme Entropy.
	 * 
	 * @return double SF Scheme Entropy.
	 */		
	public double getSFSchemeEntropy(){
		try {
			return eval.SFSchemeEntropy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}

	/**
	 * Returns the Size Of Predicted Regions.
	 * 
	 * @return double Size Of Predicted Regions.
	 */		
	public double getSizeOfPredictedRegions(){
		try {
			return eval.sizeOfPredictedRegions();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	
	/**
	 * Returns the Total Cost.
	 * 
	 * @return double Total Cost.
	 */		
	public double getTotalCost(){
		try {
			return eval.totalCost();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1/0;
	}
	
	

	
	
	/* COMMENTED OUT SINCE NOT NEEDED FOR THE NON-LINEAR REGRESSION!  R^2 IS JUST TOO CONTROVERSIAL! THEREFORE, AVOIDED.
	public double getGoodnessOfFit(){
		//"dependent variable" represents the OUTPUT (PREDICTED) or effect - Y
		//"independent variables" represent the INPUT (ACTUAL) or causes - X
		
		//Find the average of all the "y" values in the set of data pairs.

		ArrayList<Prediction> predictions = eval.predictions();
		int t=0;
		double Yavg = 0;
		for (;t<predictions.size();t++){
			Prediction p = predictions.get(t);
			Yavg = Yavg+ p.predicted();
		}
		Yavg = Yavg/(double)t;
		
		//Subtract the average y value from each individual y value and then square the difference.
		double SSTotal = 0;
		for (t=0;t<predictions.size();t++){
			Prediction p = predictions.get(t);
			double predicted = p.predicted();
			SSTotal = SSTotal + Math.pow((predicted-Yavg), 2);
		}
		
		
		//Y-hat values are X
		//Calculate the difference between the actual y value for each data point and the corresponding y hat value. 
		//Square each of these differences and then add them.

		double SSE = 0;
		for (t=0;t<predictions.size();t++){
			Prediction p = predictions.get(t);
			double predicted = p.predicted();
			double actual = p.actual();
			SSE = SSE + Math.pow((predicted-actual), 2);
		}
		
		double R2 = 0;
 
		R2 =  (SSTotal-SSE)/SSE; // ?Not good for non-linear regression.
		
		return R2;
	}
*/	
	
	
}
