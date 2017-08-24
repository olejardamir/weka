package RR.TESTING;

import java.io.File;
import java.util.ArrayList;

import RR.prediction.Predictor;
import RR.prediction.Trainer;

public class TestSVM {

	public static void main(String[] args) {
		String prefix = "prefix";
	 Trainer train = new Trainer("dataset/year.csv",
				"weka.clusterers.Canopy -N -1 -max-candidates 100 -periodic-pruning 1 -min-density 0.1 -t2 1.0 -t1 -1.0 -S 1",
				"dataset/",
				6,
				"weka.classifiers.functions.LibSVM -S 4 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -model -seed 1",
				"dataset/finalModel.model",
				"weka.classifiers.functions.LibSVM -S 4 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -model -seed 1"
				,prefix
				);
	
	
	ArrayList<String> clusters = train.getClusters();
	
 	for (int t=0;t<clusters.size();t++){
 		System.out.println(clusters.get(t));
 	}
 
		 Predictor predict = new Predictor(
				 "dataset/wheel.csv",
				 "dataset/wheelout.csv",
				 "dataset/finalModel.model",
				 clusters
				 ); 
		 
 
		
 

	}

}
