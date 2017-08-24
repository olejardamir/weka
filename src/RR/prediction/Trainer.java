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

public class Trainer {

	
	private SVMClusterer scluster = new SVMClusterer();

	
	/**
	 * This does all the necessary work in order to input a CSV file and output a new CSV file with the same information and the new column with the predicted values.
	 * 
	 * @param loadCSV The file to load and start the training.
	 * @param canopyOptions Options for a canopy clusterer.
	 * @param clusterDir The directory to save all clusters as csv and SVM models.
	 * @param outliers The minimum number (exclusive) of rows per cluster.
	 * @param SVMclusterOptions The options for training the clusters
	 * @param FinalModelFile filename and a path to save a final SVM model (that includes all clusters).
	 * @param finalModelOptions The options for training the final model.
	 * 
	 * @return void
	 */	
 
	
	public Trainer(String loadCSV, String canopyOptions, String clusterDir, int outliers,
		String SVMclusterOptions, String finalModelFile, String finalModelOptions, String prefix){
		scluster.loadCSV(loadCSV);
		scluster.buildModel(canopyOptions);		 
		scluster.canopiesToSVM(clusterDir, outliers,SVMclusterOptions, prefix);
		int csvSize=scluster.getNumberOfCSVInstances(); //System.out.println(csvSize+"*");
		int canopySize=scluster.getNumberOfClusters(); //System.out.println(canopySize+"*");
		double[][] newCSV = scluster.makeAllClustersCSV(csvSize, canopySize);
		scluster.makeModel(finalModelFile,newCSV,finalModelOptions);
		
	}
	
	
	/**
	 * Returns a number of clusters used while training
	 * 
	 * @return numberOfClusters
	 * */
	public int getNumberOfClusters(){
		return scluster.getNumberOfClusters();
	}
	
	
	/**
	 * Returns a list of clusters created while training
	 * 
	 * @return numberOfClusters
	 * */
	public ArrayList<String> getClusters(){
		return scluster.getClusters();
	}
	
	
}
