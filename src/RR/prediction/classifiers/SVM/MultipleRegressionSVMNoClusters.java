package RR.prediction.classifiers.SVM;

/**
 * This class is created for the single-column prediction with the SVM.
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

public class MultipleRegressionSVMNoClusters {
private String workingDir; //the temporary directory where all the work is done, must end with a slash
private String inputCSV; //the input for training
private String desiredCSV; //desired outcomes per each row of inputCSV
private ArrayList<String> generatedCSVs = new ArrayList<String>(); //paths to temporary CSVs
private ArrayList<String> generatedModels = new ArrayList<String>(); //paths to generated temporary SVM models



/**
 * This is a constructor method made for situations when we want to use the raw SVM predicitons only.
 * 
 * @param workingDir This is a directory where all the temporary files will be created, and SVM models saved.
 * @param inputCSV This is a file we wish to train the SVM with. The number of columns must match the number of desiredCSV columns.
 * @param desiredCSV This is a file that contains the inputCSV's expected outcome. The number of columns must match the number of inputCSV columns.
 * @param SVMOptions The options to use for training the SVM
 */	
public 	MultipleRegressionSVMNoClusters(String workingDir, String inputCSV, String desiredCSV, String SVMoptions){

	this.workingDir = workingDir;
	this.inputCSV = inputCSV;
	this.desiredCSV = desiredCSV;	
	this.generateTrainingData();
	this.generateSVMModels(SVMoptions);
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
 * Private method, generates the SVM models based on the CSV files and options.
 */
private void generateSVMModels(String SVMOptions){
	for (int t=0;t<generatedCSVs.size();t++){
		SVM svm = new SVM();
		svm.loadCSV(generatedCSVs.get(t));
		svm.buildModel(SVMOptions);
		svm.saveModelToFile(workingDir+""+t+".model");
		generatedModels.add(workingDir+""+t+".model");
	}
}



/**
 * This method predicts the data based on the inputed row. Data should be comma-delimited.
 * This works only for the single-column regression output.
 * 
 * @param row The row of data, comma-delimited.
 * @return The comma-delimited prediction.
 */
public String predictForRow(String row){
row = row+",0";
String result = "";
	for (int t=0;t<generatedModels.size();t++){
		SVM svm = new SVM();
		svm.loadModelFromFile(generatedModels.get(t));
		result = result+","+svm.predictForRow(row, ",");
		
	}
	while(result.startsWith(",")){result = result.substring(1,result.length());}
	while(result.endsWith(",")){result = result.substring(0,result.length()-1);}
	return result;
	
}


}
