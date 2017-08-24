package RR.TESTING;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.trees.ExtraTree;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class testMisc {

	public static void main(String[] args) {
		try {
	
	    
		
		// ExtraTree rss = (ExtraTree) weka.core.SerializationHelper.read("dataset/models/test.model");
		//weka.classifiers.meta.AdditiveRegression rss = (weka.classifiers.meta.AdditiveRegression) weka.core.SerializationHelper.read("dataset/models/e.model");
		// weka.classifiers.meta.RandomCommittee rss = (weka.classifiers.meta.RandomCommittee) weka.core.SerializationHelper.read("dataset/models/test.model");
		// weka.classifiers.rules.DecisionTable rss = (weka.classifiers.rules.DecisionTable) weka.core.SerializationHelper.read("dataset/models/test.model");
		//weka.classifiers.trees.RandomTree rss = (weka.classifiers.trees.RandomTree) weka.core.SerializationHelper.read("dataset/models/e.model");
		//RBFNetwork rss = (RBFNetwork) weka.core.SerializationHelper.read("dataset/models/a.model");
		//weka.classifiers.meta.Dagging rss = (weka.classifiers.meta.Dagging) weka.core.SerializationHelper.read("dataset/models/a.model");
		//weka.classifiers.functions.LibSVM rss = (weka.classifiers.functions.LibSVM) weka.core.SerializationHelper.read("dataset/models/a.model");
		// weka.classifiers.trees.RandomTree rss = (weka.classifiers.trees.RandomTree) weka.core.SerializationHelper.read("dataset/models/test.model");
		//weka.classifiers.trees.RandomForest rss = (weka.classifiers.trees.RandomForest) weka.core.SerializationHelper.read("dataset/models/ef.model");	
		// weka.classifiers.trees.M5P rss = (weka.classifiers.trees.M5P) weka.core.SerializationHelper.read("dataset/models/a.model");
		//weka.classifiers.trees.DecisionStump rss = (weka.classifiers.trees.DecisionStump) weka.core.SerializationHelper.read("dataset/models/a.model");
		//weka.classifiers.rules.ZeroR rss = (weka.classifiers.rules.ZeroR) weka.core.SerializationHelper.read("dataset/models/a.model"); 
		//weka.classifiers.rules.M5Rules rss = (weka.classifiers.rules.M5Rules) weka.core.SerializationHelper.read("dataset/models/a.model");
		// weka.classifiers.rules.DecisionTable rss = (weka.classifiers.rules.DecisionTable) weka.core.SerializationHelper.read("dataset/models/test.model");
		//weka.classifiers.functions.RBFRegressor rss = (weka.classifiers.functions.RBFRegressor) weka.core.SerializationHelper.read("dataset/models/test.model");
		//weka.classifiers.meta.RandomCommittee rss = (weka.classifiers.meta.RandomCommittee) weka.core.SerializationHelper.read("dataset/models/test.model"); 
		//weka.classifiers.meta.RegressionByDiscretization rss = (weka.classifiers.meta.RegressionByDiscretization) weka.core.SerializationHelper.read("dataset/models/test.model");
		//weka.classifiers.meta.RandomSubSpace rss = (weka.classifiers.meta.RandomSubSpace) weka.core.SerializationHelper.read("dataset/models/test.model");
			
		weka.classifiers.functions.LibSVM rss = (weka.classifiers.functions.LibSVM) weka.core.SerializationHelper.read("dataset/models/incident.model");	
			
		BufferedReader br = new BufferedReader(new FileReader("dataset/models/incidents-joined-RND-nums.csv"));

		String line;
		while ( (line = br.readLine()) != null) { //System.out.println(line);
		String[]s=line.split(",");
		double[] r = new double[s.length];
		for (int t=0;t<r.length;t++){r[t]=Double.parseDouble(s[t]);}
		
		int sz = r.length-1;
 

		ArrayList<Attribute> atts = new ArrayList<Attribute>(sz);

		for (int t = 0; t < sz + 1; t++) {
			atts.add(new Attribute("name" + t, t));
		}

		Instances dataRaw = new Instances("TestInstances", atts, sz);
		dataRaw.add(new DenseInstance(1.0, r));
		Instance first = dataRaw.firstInstance(); //System.out.println(first);
		int cIdx = dataRaw.numAttributes() - 1;
		dataRaw.setClassIndex(cIdx);

		System.out.println(rss.classifyInstance(first));
		
		}	
			
			
			
		//int[] split = {22,43,29,6,7,36,44,5,15,23,21,24,20,26,27,28,14,25,13,18,16,4,30,8,19,45,33,12,10,9,31,35,42,11,37,39,17,40,34,38,3,41,2,32,1,46,47,49,0,48,1};
		//int[] split = {22,7,43,27,29,28,36,20,44,21,25,5,6,23,13,24,14,26,15,12,35,4,18,45,19,37,8,3,11,10,16,30,33,31,17,34,40,42,2,9,1,32,38,39,46,47,49,41,48,0,1};

		
	/*	int sz = split.length - 1;
		double[] raw = new double[split.length];
		for (int t = 0; t < sz; t++) {
			raw[t] = (double)(split[t]);
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

		System.out.println(rss.classifyInstance(first));*/
		
		
		
		
		} catch (Exception e) {
	}
	}
}
