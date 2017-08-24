package RR.TESTING;
import RR.prediction.classifiers.SVM.MultipleRegressionSVM;

public class testMultipleRegression {

	public static void main(String[] args) {
		MultipleRegressionSVM test = new MultipleRegressionSVM(
				"dataset/",
				"dataset/input.csv",
				"dataset/output.csv",
				"weka.classifiers.functions.LibSVM -S 3 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -model -seed 1",
				"weka.clusterers.Canopy -N -1 -max-candidates 100 -periodic-pruning 1 -min-density 0.1 -t2 1.0 -t1 -1.0 -S 1",
				"weka.classifiers.functions.LibSVM -S 3 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -model -seed 1",
				66666666
				);
		
		String g = test.predictForRow("9,18,41,42,48,49");
System.out.println(g);








	}

}
///String workingDir, String inputCSV, String desiredCSV, String SVMoptions