package RR.interpolation.SVM;
/**
 * The main purpose of this class is to load a CSV, select the columns that have missing values, and fill-in those values by selecting the training/predicting columns.
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

import RR.prediction.classifiers.SVM.MultipleRegressionSVM;


public class Interpolate {
private String[][] matrix; //CSV converted to a String rowsXcolumns
private ArrayList<String[]> toConvert= new ArrayList<String[]>();	//helper to convert CSV to a String
private int matrixRows;
private int matrixColumns;
private ArrayList<String> forTraining = new ArrayList<String>();
private MultipleRegressionSVM test;


/**
 * This is a constructor method to be used if canopy clustering is to be included in a regression.
 * @param CSVin
 * @param CSVout
 * @param predictFrom
 * @param predictTo
 * @param nullMark
 * @param workingDir
 * @param SVMoptions
 * @param canopyOptions
 * @param finalSVMOptions
 * @param outliers
 */
//for use with canopies!
public Interpolate(String CSVin, String CSVout, int[] predictFrom, int predictTo, String nullMark,
String workingDir, String SVMoptions, String canopyOptions, String finalSVMOptions, int outliers){
	loadCSV(CSVin, nullMark); 
	separate(predictFrom, predictTo); 
	saveToTmp(workingDir);  
	trainSVM(workingDir,SVMoptions,canopyOptions,finalSVMOptions,outliers); 
	predictAndPopulate(predictFrom,predictTo); 
	saveCSV(CSVout); 
}

/**
 * This is a constructor method to do the regression without canopy clustering.
 * @param CSVin
 * @param CSVout
 * @param predictFrom
 * @param predictTo
 * @param nullMark
 * @param workingDir
 * @param finalSVMOptions
 */
//for use without canopies!
public Interpolate(String CSVin, String CSVout, int[] predictFrom, int predictTo, String nullMark,
String workingDir, String finalSVMOptions){
	loadCSV(CSVin, nullMark);
	separate(predictFrom, predictTo);
	saveToTmp(workingDir);
	trainSVM(workingDir,finalSVMOptions);
	predictAndPopulate(predictFrom,predictTo);
	saveCSV(CSVout);
}

/**
 * This method saves the result to a CSV file.
 * @param CSVout The name of the CSV file to save the results.
 */
private void saveCSV(String CSVout){
	try{
		BufferedWriter out = new BufferedWriter(new FileWriter(CSVout));
		
		for (int r=0;r<matrixRows;r++){
			String w = "";
			for (int c=0;c<matrixColumns;c++){
				w = w+matrix[r][c]+",";
			}
			w = w.substring(0,w.length()-1);
			out.write(w+"\n");
		}
		out.close();
	}catch(Exception e){e.printStackTrace();}		
}


/**
 * This method predicts and populates the column's missing values
 * @param predictFrom
 * @param predictTo
 */
private void predictAndPopulate(int[] predictFrom, int predictTo){
	for (int r=0;r<matrixRows;r++){
		String in = "";
		for (int t=0;t<predictFrom.length;t++){in = in+matrix[r][predictFrom[t]]+",";}
		in = in.substring(0,in.length()-1);
		if (matrix[r][predictTo].equals("NULL") && !in.contains("NULL")){
			while(in.endsWith(",")){in = in.replace(",", "");}
			String g = test.predictForRow(in);
			matrix[r][predictTo] = g;
		}
		
		 
	}
}

/**
 * This method creates the training matrix, from the selected columns, ignoring any possible missing values
 * @param predictFrom
 * @param predictTo
 */
//populate the training matrix
private void separate(int[] predictFrom, int predictTo){
for (int r=0;r<matrixRows;r++){
	String in = "";
	for (int t=0;t<predictFrom.length;t++)
	{in = in+matrix[r][predictFrom[t]]+","; }
	in = in+matrix[r][predictTo];  
	if (!in.contains("NULL")){forTraining.add(in);}
}
}


/**
 * This method saves the work into a Temporary (workingDir) folder, to continue with the predictions.
 * @param workingDir
 */
private void saveToTmp(String workingDir){
	try{
		BufferedWriter out = new BufferedWriter(new FileWriter(workingDir+"tmpIN.csv"));
		BufferedWriter out2 = new BufferedWriter(new FileWriter(workingDir+"tmpDESIRED.csv"));
		for (int t=0;t<forTraining.size();t++){
			out.write(forTraining.get(t).substring(0,forTraining.get(t).lastIndexOf(","))+"\n");
			out2.write(forTraining.get(t).substring(forTraining.get(t).lastIndexOf(",")+1, forTraining.get(t).length())+"\n");
		}
		out.close();
		out2.close();
	}catch(Exception e){e.printStackTrace();}	
}


/**
 * This method is used to train the SVMs with canopy clustering.
 * @param workingDir
 * @param SVMoptions
 * @param canopyOptions
 * @param finalSVMOptions
 * @param outliers
 */
//train the SVM  for use with canopies!
private void trainSVM(String workingDir, String SVMoptions, String canopyOptions, String finalSVMOptions, int outliers){
test = new MultipleRegressionSVM(
		workingDir,
		workingDir+"tmpIN.csv",
		workingDir+"tmpDESIRED.csv",
		SVMoptions,
		canopyOptions,
		finalSVMOptions,
		outliers
			);
}

/**
 * This method is used to train the SVMs, without the canopy clustering.
 * @param workingDir
 * @param finalSVMOptions
 */
//train the SVM  for use WITHOUT canopies!
private void trainSVM(String workingDir, String finalSVMOptions){
test = new MultipleRegressionSVM(
		workingDir,
		workingDir+"tmpIN.csv",
		workingDir+"tmpDESIRED.csv",
		finalSVMOptions
			);
}


/**
 * This method is used to load the CSV into a working memory, and convert it into a matrix.
 * @param filename
 * @param nullMark
 */
	//Convert CSV to Matrix
	//filename: the csv file
    //nullmark: how is the missing-cell marked after the database to CSV call ? is it "Null", "" or something else?
	private void loadCSV(String filename, String nullMark){
		try{
			
			BufferedReader br = new BufferedReader(new FileReader(filename));
			int rows = 1;
			String line=br.readLine();
			while (line.contains(",,")){line = line.replace(",,", "NULL");}
			
			String[] s = line.split(",");
			toConvert.add(s);
			int columns = s.length;
			
			while ((line =br.readLine()) != null) {
				while (line.contains(",,")){line = line.replace(",,", "NULL");}
				s = line.split(",");
				toConvert.add(s);
				rows++;
			}
			br.close();
			
			matrix = new String[rows][columns];
			this.matrixRows = rows;
			this.matrixColumns = columns;
			
			//now convert ArrayList to String[][]
			//String NULL stands for an empty cell
			for (int r=0; r<rows; r++){
				String[] g = toConvert.get(r);  
				for (int c=0; c<columns; c++){
					if (!g[c].equals("") && !g[c].equals("NULL") &&!g[c].equals(nullMark)) 
					{matrix[r][c] = g[c];}
					else {matrix[r][c]="NULL";}
				}
			}
		 
			
		}catch(Exception e){e.printStackTrace();}
	}
	
 
	
 
	
}
