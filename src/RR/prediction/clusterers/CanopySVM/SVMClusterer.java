package RR.prediction.clusterers.CanopySVM;

/**
 * This class is created to help the SVM classifications with clustering algorithms.
 * <p>
 * @author      Damir Olejar
 * @version     1.0
 * @version		WEKA 3.7
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import RR.prediction.classifiers.SVM.SVM;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class SVMClusterer {
	
 
	private Instances data;
    private weka.clusterers.Canopy canopy;
	private ArrayList<String> filenames; //this becomes the list of clustered SVM model files
	
	
	/**
	 * Returns a number of clusters used while training
	 * 
	 * @return numberOfClusters
	 * */
	public int getNumberOfClusters(){
		return filenames.size();
	}
	
	/**
	 * Returns a number of CSV Instances (rows in CSV)
	 * 
	 * @return numberOfClusters
	 * */
	public int getNumberOfCSVInstances(){
		return this.data.size();
	}
	
	
	
	/**
	 * This method gets the list of filenames of clusters
	 * 
	 * @param filename The destination to a CSV file
	 * */
	public ArrayList<String> getClusters() {
		return this.filenames;
	}
	
	/**
	 * This method sets the list of filenames of clusters
	 * 
	 * @param filename The destination to a CSV file
	 * */
	public void setClusters(ArrayList<String> files){
		this.filenames = files;
	}
	
	/**
	 * Returns a Canopy model object that was loaded or trained.
	 * 
	 * @return Canopy model object that was loaded or trained.
	 */
	public weka.clusterers.Canopy getCanopy() {
		return this.canopy;
	}

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
	 * @param filemane The destination to a CSV file
	 * @return void
	 */
	public void loadCSV(String filename) {
		try {
			CSVLoader trainingLoader = new CSVLoader();
			trainingLoader.setSource(new File(filename));
			trainingLoader.setNoHeaderRowPresent(true);
			data = trainingLoader.getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	/**
	 * This method trains and builds a clusterer based on a loaded CSV file.
	 * Please note that it is important to include all proper parameters, and to
	 * do that, please use WEKA explorer GUI
	 * 
	 * @param options The Canopy training options (or parameters).
	 * @return void
	 */
	public void buildModel(String options) {
		try {
			canopy = new weka.clusterers.Canopy();
			String[] optionsArr = weka.core.Utils.splitOptions(options);
			canopy.setOptions(optionsArr);
			canopy.buildClusterer(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
 
	
	
	/**
	 * This method selects and saves the Cannopies to SVM models
	 * Nevertheless, other clustering algorithms from WEKA can also be used:
	 * Xmeans, SimpleKMeans, sIB, SelfOrganizing, map, MakeDenistyBasedClusterer, LVQ, FilteredClusterer, FarthestFirst.
	 * 
	 * Canopy is used because it is known to improve k-means clustering, and because it is fast for big-data.
	 * 
	 * @param dir1 Location to where to save clusters as cvs. dir1 must end with a slash indicating a directory.
	 * @param outliers The minimum number (exclusive) of rows per cluster. If below minimum, data is treated as outliers. Suggested minimum number: 60.
	 * @param options These are the Canopy clustering options.
	 * @return void
	 */
	public void canopiesToSVM(String dir1, int outliers, String options, String prefix) {
		try {
			CanopyWork cworks = new CanopyWork();
			int canopySize = canopy.numberOfClusters();
			int csvSize = data.size();
			Instance[][] matrix = cworks.matrix(canopySize, csvSize, data,
					canopy);
			filenames = cworks.saveClusters(matrix, outliers, prefix, dir1); // csv filenames
			filenames = cworks.trainAndSaveClusters(filenames, options); // model filenames

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This method is used to generate CSVs of all generated clusters. 
	 * Purpose of this method is to convert the bucket-sorted Instances to CSV data with clustered data-analysis.
	 * @param csvSize The size (number of rows) of the loaded CSV
	 * @param canopySize The number of clusters used to aid predictions
	 */
	public double[][] makeAllClustersCSV(int csvSize, int canopySize) {
		double[][] newCSV = null;
		try {
 
			newCSV = new double[csvSize][canopySize]; // row,column: e,t

			for (int t = 0; t < filenames.size(); t++) {
				SVM svm = new SVM();
				svm.loadModelFromFile(filenames.get(t));
				
				for (int e = 0; e < csvSize; e++) {

					if (data.classIndex() == -1)
						data.setClassIndex(data.numAttributes() - 1);

					Instance ins = data.get(e); 
					double d = svm.classifyInstance(ins);
					
					newCSV[e][t] = d;
				}

			}
 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newCSV;
	}
	
 
	/**
	 * This method is used to save the trained SVM models. 
	 * @param saveTo The filename path to save the trained SVM.
	 * @param newCSV The bucket-sorted matrix of Instances
	 * @param options The options for generating the SVM models of the clusters.
	 * @return void
	 */
	public void makeModel(String saveTo, double[][] newCSV, String options) {
		try {
			int csvSize = data.size();
			CanopyWork cw = new CanopyWork();
			String row = "";
			boolean instancesExist = false;
			Instances i = null;

			for (int q = 0; q < csvSize; q++) {
				for (int w = 0; w < filenames.size(); w++) {
					row = row + newCSV[q][w] + ",";
				}
				while (row.startsWith(",")){row=row.substring(1,row.length());}
				while (row.endsWith(",")){row=row.substring(0,row.length()-1);}
				if (row.length()>0){row = row +",";}
				row = row + data.get(q); 
				Instance ins = cw.makeInstance(row, ",");

				// if Instances has not been initiated (must be done this way
				// unfortunately)
				if (!instancesExist) {
					instancesExist = true;
					String[] split = row.split(",");
					int sz = split.length;
					ArrayList<Attribute> atts = new ArrayList<Attribute>(sz);
					for (int t = 0; t < sz; t++) {
						atts.add(new Attribute("name" + t, t));
					}
					i = new Instances("AddedClusteredInstances", atts, sz);
				}

				i.add(ins);
				row = "";

			}
			int cIdx = i.numAttributes() - 1;
			i.setClassIndex(cIdx);

			// train and save the model!
			SVM finalModel = new SVM();
			finalModel.setInstances(i);
			finalModel.buildModel(options);
			finalModel.saveModelToFile(saveTo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to output the predictions into a CSV. 
	 * @param filename The name of the file.
	 * @param newCSV The bucket-sorted matrix of Instances
	 * @param modelFile The trained SVM file.
	 * @return void
	 */	
	public void outputPredictions(String filename, double[][] newCSV, String modelFile) {
		try {
			int csvSize = data.size();
			CanopyWork cw = new CanopyWork();
			String row = "";
			boolean instancesExist = false;
			Instances i = null;

			if(filenames!=null){
			for (int q = 0; q < csvSize; q++) {
				for (int w = 0; w < filenames.size(); w++) {
					row = row + newCSV[q][w] + ",";
				}
				while (row.startsWith(",")){row=row.substring(1,row.length());}
				while (row.endsWith(",")){row=row.substring(0,row.length()-1);}
				if (row.length()>0){row = row +",";}
				row = row + data.get(q); 
				Instance ins = cw.makeInstance(row, ",");

				// if Instances has not been initiated (must be done this way unfortunately)
				if (!instancesExist) {
					instancesExist = true;
					String[] split = row.split(",");
					int sz = split.length;
					ArrayList<Attribute> atts = new ArrayList<Attribute>(sz);
					for (int t = 0; t < sz; t++) {
						atts.add(new Attribute("name" + t, t));
					}
					i = new Instances("AddedClusteredInstances", atts, sz);			
				}
				i.add(ins);
				row = "";
			}
			int cIdx = i.numAttributes() - 1;
			i.setClassIndex(cIdx);
			SVM finalModel = new SVM();
			finalModel.loadModelFromFile(modelFile);

			// write predictions
			predictionsToCSV(finalModel, i, filename);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
		
	/**
	 * This method is used to write the predictions to file. Private method
	 */
	
	private void predictionsToCSV(SVM finalModel, Instances ins, String filename) {
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(filename));
			for (int t = 0; t < ins.size(); t++) {
				Instance e = ins.get(t);
				String line = e.toString(); //System.out.println(e);
				double predicted = finalModel.classifyInstance(e);
				line = line + "," + predicted;
				output.write(line+"\n");
				
			}
			output.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}


		
	}
	
 
