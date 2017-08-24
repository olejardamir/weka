package RR.TESTING;

import RR.prediction.ModelInformation;

public class testSVMInfo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
ModelInformation inf = new ModelInformation("dataset/finalModel.model");
System.out.println(inf.getModelInfo());
	}

}
