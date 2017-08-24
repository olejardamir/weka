package RR.TESTING;

import RR.prediction.evaluation.findSVMParameters;

public class testFindParameters {

	public static void main(String[] args) {
		findSVMParameters fp = new findSVMParameters();
	fp.searchBestOptions("dataset/test.csv", "dataset/test.csv");

	}

}
