package RR.prediction.classifiers.SVM;

/**
 * This class is created in order to minimize the coding necessary to work with the SVM models.
 * Nevertheless, the certain sequence of steps must be followed in order to make this class work properly.
 * <p>
 * In order to CREATE A NEW SVM model and then save it, the user should:
 * Create an instance of the SVM class [example: SVM test = new SVM();]
 * Load the CSV file to create the model with [example: test.loadCSV("test.csv");]
 * Start training the model with the SVM parameters 
 * [example: test.buildModel("weka.classifiers.functions.LibSVM -S 3 -K 1 -D 3 -G 0.1 -R 1.0 -N 0.5 -M 40.0 -C 10000000 -E 1.0E-4 -P 1.3 -model -seed 1");]
 * Save the model to a file [example: test.saveModelToFile("dataset/model1.model");]
 * <p>
 * In order to LOAD SVM MODEL FROM A FILE, and predict for the CSV the user should:
 * Create an instance of the SVM class [example: SVM test = new SVM();]
 * Load the CSV file that contains the data to run a prediction to [example: test.loadCSV("test.csv");]
 * Load the SVM model [example: test.loadModelFromFile("dataset/model1.model");]
 * Do the prediction on a loaded model for each instance using the getInstance() method.
 * <p>
 * In order to LOAD THE SVM MODEL FROM A FILE, and predict for just one string-row, the user should:
 * Create an instance of the SVM class [example: SVM test = new SVM();]
 * Run the predict for row method [example: test.predictForRow("1076,2801,6,2,1,?", ",");]
 * <p>
 * @author      Damir Olejar
 * @version     1.0
 * @version		WEKA 3.7
 */


import java.io.File;

import weka.core.Attribute;

import java.util.ArrayList;

import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.classifiers.functions.LibSVM;

public class SVM {

	private LibSVM model; 
	private Instances data;

	/**
	 * Returns a LibSVM model object that was loaded or trained.
	 * 
	 * @return LibSVM model object that was loaded or trained.
	 */
	public LibSVM getModel() {
		return this.model;
	}
	
	
	/**
	 * Sets a LibSVM model object.
	 * 
	 * @return void
	 */
	public void  setModel(LibSVM model) {
		 this.model=model;
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
	 * Set Instances
	 * 
	 */
	public void setInstances(Instances instances) {

		this.data = instances;
	}

	/**
	 * This method loads the CSV file to Instances
	 * 
	 * @param filename The destination to a CSV file
	 * @return void
	 */
	public void loadCSV(String filename) {
		try {
			CSVLoader trainingLoader = new CSVLoader();
			trainingLoader.setSource(new File(filename));
			trainingLoader.setNoHeaderRowPresent(true);
			data = trainingLoader.getDataSet();
			if (data.classIndex() == -1)
				data.setClassIndex(data.numAttributes() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method trains and builds a model based on a loaded CSV file. Please
	 * note that it is important to include all proper parameters, and to do
	 * that, please use WEKA explorer GUI
	 * 
	 * @param options The SVM training options (or parameters).
	 * @return void
	 */
	public void buildModel(String options) {
		try {
			model = new LibSVM();
			String[] optionsArr = weka.core.Utils.splitOptions(options);
			model.setOptions(optionsArr);
			model.buildClassifier(this.data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method saves the trained model to a file
	 * 
	 * @param filename Desired destination to save the SVM model
	 * @return void
	 */
	public void saveModelToFile(String filename) {
		try {
			weka.core.SerializationHelper.write(filename, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method loads the trained model from a a saved file Please note that
	 * it is important to include all proper parameters, and to do that, please
	 * use WEKA explorer GUI
	 * 
	 * @param filename Desired destination to load the SVM model from
	 * @return void
	 */
	public void loadModelFromFile(String filename) {
		try {
			model = (LibSVM) weka.core.SerializationHelper.read(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is made to quickly predict just one row of input data Please
	 * note that this method accepts only numeric values, while the last value
	 * can be anything (but must be included in a row), since it is the value to
	 * be predicted
	 * 
	 * @param row The delimited row of values to make a prediction from
	 * @param delimiters The delimiters used to delimit the values in a row (for example, comma, tab, space,...)
	 * @return double the prediction value
	 */
	public double predictForRow(String row, String delimiters) {
		try {

			String[] split = row.split(delimiters);
			int sz = split.length - 1;
			double[] raw = new double[split.length];
			for (int t = 0; t < sz; t++) {
				raw[t] = Double.parseDouble(split[t]);
			}

			ArrayList<Attribute> atts = new ArrayList<Attribute>(sz);

			for (int t = 0; t < sz + 1; t++) {
				atts.add(new Attribute("name" + t, t));
			}

			Instances dataRaw = new Instances("TestInstances", atts, sz);
			dataRaw.add(new DenseInstance(1.0, raw));
			Instance first = dataRaw.firstInstance(); //System.out.println(first);
			int cIdx = dataRaw.numAttributes() - 1;
			dataRaw.setClassIndex(cIdx);

			return model.classifyInstance(first);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}
	
	/**
	 * This method is made to classify a single instance.
	 * 
	 * @param instance The instance to classify
	 * @return double the prediction value
	 */	
	public double classifyInstance(Instance instance){
		
		
		double ret = 0;
		try {
			ret =  model.classifyInstance(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	

}
