package RR.prediction.classifiers.SVM;

/**
 * This class is created in order to enable and combine the multiple as well as the single regression with the SVM.
 * Furthermore, the canopy clustering is used to train and then evaluate the data.
 * Canopy clustering can be turned ON and OFF, depending on whether we want to use it (since it may or may not improve the results).
 * It uses the MultipleRegressionSVMNoClusters class, which would normally be only within the scope of this class but is not due to flexibility.
 * <p>
 * @author      Damir Olejar
 * @version     1.0
 * @version		WEKA 3.7
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import RR.prediction.Trainer;

public class MultipleRegressionSVM {
private String workingDir; //the temporary directory where all the work is done, must end with a slash
private String inputCSV; //the input for training
private String desiredCSV; //desired outcomes per each row of inputCSV
private String svmOptions;
private ArrayList<String> generatedCSVs = new ArrayList<String>(); //paths to temporary CSVs
private ArrayList<String> canopyAndModels = new ArrayList<String>(); //the list of columns and canopies that were used to train the prediction; syntax: <column><canopy>
private boolean noClusters=false; //tells whether we are using the clusters or not
private MultipleRegressionSVMNoClusters reg; //This is relevant only when clusters are not used



/**
 * Sets the clustering option to OFF
 * 
 * @return void
 */
public void setClusteringOFF(){this.noClusters=true;}

/**
 * Sets the clustering option to ON
 * 
 * @return void
 */
public void setClusteringON(){this.noClusters=false;}


/**
 * This is a constructor method made for situations when we do not wish to apply the clustering.
 * 
 * @param workingDir This is a directory where all the temporary files will be created, and SVM models saved.
 * @param inputCSV This is a file we wish to train the SVM with. The number of columns must match the number of desiredCSV columns.
 * @param desiredCSV This is a file that contains the inputCSV's expected outcome. The number of columns must match the number of inputCSV columns.
 * @param SVMOptions The options to use for training the SVM
 */
public 	MultipleRegressionSVM(String workingDir, String inputCSV, String desiredCSV, String SVMoptions){
	this.noClusters=true;
	this.workingDir = workingDir;
	this.inputCSV = inputCSV;
	this.desiredCSV = desiredCSV;
	this.svmOptions = SVMoptions;
	this.generateTrainingData();
	this.generateSVMModels(SVMoptions,
			null,
			0); //null and 0 are the not-needed parameters

}

/**
 * This is a constructor method made for training the SVM with the help of canopy clustering.
 * 
 * @param workingDir This is a directory where all the temporary files will be created, and SVM models saved.
 * @param inputCSV This is a file we wish to train the SVM with. The number of columns must match the number of desiredCSV columns.
 * @param desiredCSV This is a file that contains the inputCSV's expected outcome. The number of columns must match the number of inputCSV columns.
 * @param SVMOptions The options to use for training the clusters with SVMs.
 * @param canopyOptions The options used to create the canopy clusters.
 * @param finalSVMOptions The SVM options used to generate the final prediction, utilizing the clusters' results.
 * @param outliers The minimum number (exclusive) of rows per cluster. 
 */
public 	MultipleRegressionSVM(String workingDir, String inputCSV, String desiredCSV, String SVMoptions, String canopyOptions, String finalSVMOptions, int outliers)
{
	this.noClusters=false;
	this.workingDir = workingDir;
	this.inputCSV = inputCSV;
	this.desiredCSV = desiredCSV;	
	this.svmOptions = finalSVMOptions;
	this.generateTrainingData();
	this.generateSVMModels(SVMoptions,  canopyOptions,  outliers);
}


/**
 * Private method, generates the CSVs to create the models from
 */
private void generateTrainingData(){
try{
	boolean setColumns = false;
	int columns = 2;
	for (int t=0;t<columns; t++){
		
		BufferedReader brDesired = new BufferedReader(new FileReader(this.desiredCSV));
		BufferedReader brInputCSV = new BufferedReader(new FileReader(this.inputCSV));
		BufferedWriter out = new BufferedWriter(new FileWriter(this.workingDir+""+t+".csv"));
		generatedCSVs.add(this.workingDir+""+t+".csv");
		
		String line;
		String row;
		while ( (line = brDesired.readLine()) != null && (row=brInputCSV.readLine())!=null) {
		String[] s = line.split(",");
		
		//set the number of output columns, do not repeat this step if already set.
		if (!setColumns){
			setColumns = true; 
			columns = s.length;
		}
		
		//concat. the input with a desired value.
		out.write(row+","+s[t]+"\n");
		}
		brDesired.close();
		out.close();
		brInputCSV.close();
		
	}
}catch (Exception e){e.printStackTrace();}
}

/**
 * Private method, generates and saves the SVM models in the temporary directory.
 */
private void generateSVMModels(String SVMoptions, String canopyOptions, int outliers){
		for (int t = 0; t < generatedCSVs.size(); t++) {

			if (noClusters != true) {
				String prefix = "column_" + t + "_canopy_";

				Trainer train = new Trainer(generatedCSVs.get(t),
						canopyOptions, workingDir, outliers, SVMoptions,
						workingDir + "" + t + ".model", this.svmOptions, prefix);

				// add all data to canopyAndModels
				ArrayList<String> clusters = train.getClusters();
				for (int tt = 0; tt < clusters.size(); tt++) {
					String modelName = t + ".model";
					String toAdd = "<" + workingDir + modelName + "><"
							+ clusters.get(tt) + ">";
					canopyAndModels.add(toAdd);
				}
				if (canopyAndModels.size() < 1) {
					this.noClusters = true;
					this.reg = new MultipleRegressionSVMNoClusters(workingDir,
							inputCSV, desiredCSV, svmOptions);
				}

			} else {
				this.reg = new MultipleRegressionSVMNoClusters(workingDir,
						inputCSV, desiredCSV, svmOptions);
			}

		}
}

/**
 * This method predicts the data based on the inputed row. Data should be comma-delimited.
 * This works for both, single and multiple regression.
 * 
 * @param row The row of data, comma-delimited.
 * @return The comma-delimited prediction.
 */
public String predictForRow(String row){
if (noClusters!=false){	
return noClustersRegression(row);
}

row = row+",0";
String returnResult="";
int cnt = canopyAndModels.size();
	for (int t=0;t<cnt;t++){
		String parse = canopyAndModels.get(t);
		String finalModel = parse.substring(1,parse.indexOf("><"));
		String result = row;
					while(parse.contains("<"+finalModel+"><") && t<cnt){
					String canopyModel = parse.substring(parse.indexOf("><")+2,parse.length()-1);
					SVM svm = new SVM();
					svm.loadModelFromFile(canopyModel);
					result = svm.predictForRow(row, ",")+","+result;
					t++;
						if (t<cnt){
						parse = canopyAndModels.get(t);
						}
					}
					if (t>cnt){break;}
					
					SVM svm = new SVM();
					svm.loadModelFromFile(finalModel);
					returnResult = returnResult+","+(svm.predictForRow(result, ","));
					t--; //this is a bugfix :)
	}
	while(returnResult.startsWith(",")){returnResult = returnResult.substring(1,returnResult.length());}
	while(returnResult.endsWith(",")){returnResult = returnResult.substring(0,returnResult.length()-1);}
	return returnResult;
	
}


/**
 * Private method for returning the no-clustering predicted data based on an input row. 
 */
private String noClustersRegression(String row){
	String g = reg.predictForRow(row);
	return g;
} 







}
