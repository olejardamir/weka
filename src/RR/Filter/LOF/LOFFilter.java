package RR.Filter.LOF;

/**
 * The main purpose of this class is to load a CSV, and add the LOF column to aid the SVM training. The results can be improved by using this approach.
 * Local Outlier Factor is a JAR external to WEKA, and it needs to be downloaded/included separately.
 * This class works with CSVs and temporary directories instead of loading everything into working memory.
 * 
 * USAGE EXAMPLE:
 * LOFFilter lf = new LOFFilter();
 * String options = "-min 10 -max 40 -A \"weka.core.neighboursearch.LinearNNSearch -A \\\"weka.core.EuclideanDistance -R first-last\\\"\" -num-slots 1";
 * lf.loadCSV("dataset/input.csv",",","dataset/");
 * lf.calculateLOF(options);
 * lf.writeNewCSV("dataset/input2.csv");
 * <p>
 * @author      Damir Olejar
 * @version     1.0
 * @version		WEKA 3.7
 *
 * 		
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;

public class LOFFilter {

	
	private Instances data;
	private Instances newData;
	private String delimiter = "";
	private ArrayList<String> rightmostColumn = new ArrayList<String>();
	
	
	/**
	 * Returns a set of Instances created by loading a CSV file.
	 * 
	 * @return Instances Instances data representing a CSV file.
	 */
	public Instances getInstances() {
		return this.data;
	}
	
	
	
	/**
	 * This method loads the CSV file to Instances
	 * 
	 * @param filename The destination to a CSV file
	 * @return void
	 */
	public void loadCSV(String filename, String delim, String tempDir) {
		this.delimiter = delim;
		//pre-process the CSV into tempInstances.csv
		preProcessCSV(filename, delim,tempDir);
		//load the temp file
		try {
			CSVLoader trainingLoader = new CSVLoader();
			trainingLoader.setSource(new File(tempDir+"tempInstances.csv"));
			trainingLoader.setNoHeaderRowPresent(true);
			data = trainingLoader.getDataSet();
			if (data.classIndex() == -1)
				data.setClassIndex(data.numAttributes() - 1);
			
		//remove the temp file.
		new File(tempDir+"tempInstances.csv").delete();
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * This method pre-processes the CSV into a temporary CSV (that is deleted at the end).
	 * 
	 * @param filename
	 * @param delim
	 * @param tempDir
	 */
	
	private void preProcessCSV(String filename, String delim, String tempDir){
		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			BufferedWriter out = new BufferedWriter(new FileWriter(tempDir+"tempInstances.csv"));
			String line;
			while ( (line = br.readLine()) != null) {
				
				rightmostColumn.add(line.substring(line.lastIndexOf(delim),line.length()));
				line = line.substring(0,line.lastIndexOf(delim));
				out.write(line+"\n");
			}
			br.close();
			out.close();
			}catch(Exception e){e.printStackTrace();} 
	}
	
	/**
	 * This method calculates the LOF.
	 * 
	 * @param options
	 */
	
	public void calculateLOF(String options){
		try{
		 weka.filters.unsupervised.attribute.LOF lofFilter = new weka.filters.unsupervised.attribute.LOF();
		 String[] optionsArr = weka.core.Utils.splitOptions(options);
		 lofFilter.setOptions(optionsArr);
		 lofFilter.setInputFormat(data);
		 this.newData = Filter.useFilter(data, lofFilter);
		}catch(Exception e){e.printStackTrace();}
	}
	
	
	/**
	 * This method writes the new CSV with the LOF column (as the rightmost column in CSV) and removes the temporary file.
	 * 
	 * @param filename
	 */
	
	public void writeNewCSV(String filename){
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			
			for (int t=0;t<data.size();t++){
				String line = ""+newData.get(t);
				String lofColumn = line.substring(line.lastIndexOf(this.delimiter),line.length());
				line = line.substring(0,line.lastIndexOf(this.delimiter));
				line = lofColumn +this.delimiter+line+rightmostColumn.get(t);
				line = line.substring(1,line.length());
				out.write(line+"\n");
			}
			
			out.close();
			
		}catch(Exception e){e.printStackTrace();} 
	}
	
	
}
