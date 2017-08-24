package RR.TESTING;

import RR.Filter.LOF.LOFFilter;

public class testFilter {

	public static void main(String[] args) {
		LOFFilter lf = new LOFFilter();
		String options = "-min 10 -max 40 -A \"weka.core.neighboursearch.LinearNNSearch -A \\\"weka.core.EuclideanDistance -R first-last\\\"\" -num-slots 1";
		lf.loadCSV("dataset/input.csv",",","dataset/");
		lf.calculateLOF(options);
		lf.writeNewCSV("dataset/input2.csv");
	}

}
