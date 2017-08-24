package RR.TESTING;

import RR.prediction.classifiers.SVM.MultipleRegressionSVM;

public class TestMultipleRegressionClusters {

	public static void main(String[] args) {
		
	
		//Test for multiple regression with canopies
		MultipleRegressionSVM test = new MultipleRegressionSVM(
				"dataset/",
				"dataset/suites_flat_test1.csv",
				"dataset/suites_flat_test1_out.csv",
				"weka.classifiers.functions.LibSVM -S 3 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -model -seed 1",
				"weka.clusterers.Canopy -N -1 -max-candidates 100 -periodic-pruning 1 -min-density 0.1 -t2 1.0 -t1 -1.0 -S 1",
				"weka.classifiers.functions.LibSVM -S 3 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -model -seed 1",
				2
				);
		
		String g = test.predictForRow("1,2");
		System.out.println(g+"---------------------");
		
		
		
		
		//Test for multiple regression without canopies
		MultipleRegressionSVM test2 = new MultipleRegressionSVM(
				"dataset/",
				"dataset/suites_flat_test1.csv",
				"dataset/suites_flat_test1_out.csv",
				"weka.classifiers.functions.LibSVM -S 3 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -model -seed 1"
				);
		
		String g2= test2.predictForRow("1,2");
		System.out.println(g2+"---------------------");
		
		
	}
	

}
///String workingDir, String inputCSV, String desiredCSV, String SVMoptions