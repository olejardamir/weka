package RR.TESTING;

import RR.interpolation.SVM.Interpolate;

public class testInterpolation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] from = {0,1,2,3};

		//testing with canopy
		Interpolate i = new Interpolate(
				"dataset/suites_flat.csv",
				"dataset/suites_flat_out.csv",
				from,
				4,
				"NULL",
				"dataset/",
				"weka.classifiers.functions.LibSVM -S 3 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -model -seed 1",
				"weka.clusterers.Canopy -N -1 -max-candidates 100 -periodic-pruning 1 -min-density 0.1 -t2 1.0 -t1 -1.0 -S 1",
				"weka.classifiers.functions.LibSVM -S 3 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -model -seed 1",
				2
				);
 
//testing without canopy
	Interpolate i2 = new Interpolate(
			"dataset/suites_flat.csv",
			"dataset/suites_flat_out.csv",
			from,
			4,
			"NULL",
			"dataset/",
			"weka.classifiers.functions.LibSVM -S 3 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -model -seed 1"
			);
 

	}
	//String CSVin, String CSVout, int[] predictFrom, int predictTo, String nullMark,
	//String workingDir, String finalSVMOptions
	
	
}
