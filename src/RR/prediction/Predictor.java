package RR.prediction;

/**
 * This class is created in order to do all the necessary work for predicting the data from a CSV, and writing it into a new CSV.
 * <p>
 * @author      Damir Olejar
 * @version     1.0
 * @version		WEKA 3.7
 */

import java.util.ArrayList;


import RR.prediction.clusterers.CanopySVM.SVMClusterer;



/**
 * This does all the necessary work in order to input a CSV file and output a new CSV file with the same information and the new column with the predicted values.
 * 
 * @param predictCSV The CSV containing the data from which to make a prediction.
 * @param outputCSV The CSV to save all predictions with the original data.
 * @param finalModel SVM model previously trained with N number of clusters and the inputted CSV data.
 * @param clusters The list of cluster models to use.
 * 
 * @return void
 */
public class Predictor {
 
	
	//predictCSV- the csv that contains the predicting data
	//outputCSV- where to save the CSV with original+predicted data
	//finalModel - the location of the final SVM model (trained with clusters+original data)
	
	public Predictor(String predictCSV, String outputCSV, String finalModel, ArrayList<String> clusters){
	SVMClusterer scluster = new SVMClusterer();
	scluster.setClusters(clusters);
	scluster.loadCSV(predictCSV);
	int csvSize=scluster.getNumberOfCSVInstances();
	int canopySize=scluster.getNumberOfClusters();
	double[][] newCSV2 = scluster.makeAllClustersCSV(csvSize, canopySize); 
	scluster.outputPredictions(outputCSV, newCSV2,finalModel);
	}
	
	

	
}
