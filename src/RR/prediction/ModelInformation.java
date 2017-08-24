package RR.prediction;
/**
 * This class is created in order to output the information regarding the model file
 * <p>
 * @author      Damir Olejar
 * @version     1.0
 * @version		WEKA 3.7
 */

import weka.classifiers.functions.LibSVM;

public class ModelInformation {
	private LibSVM model; 
	private String filename;
	
	/**
	 * Constructor accepting a model filename, in order to load it.
	 * 
	 * @return String The String representation of the loaded model
	 */	
	public ModelInformation(String filename) {
		try {
			model = (LibSVM) weka.core.SerializationHelper.read(filename);
			this.filename = filename;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * This method displays all information as a string regarding the loaded model with a constructor method
	 * 
	 * @return String The String representation of the loaded model
	 */	
 
	
	public String getModelInfo(){
		
		String s = "=================================\n"+"The text presentation of the:\n"+filename+"\n=================================\n";
		
		s = s+ model.SVMTypeTipText()+": "+model.getSVMType().toString()+"\n"; 	
		s = s+ model.cacheSizeTipText()+": "+model.getCacheSize()+"\n";
		s = s+ model.coef0TipText()+": "+model.getCoef0()+"\n";
		s = s+ model.costTipText()+": "+model.getCost()+"\n";
		s = s+ model.debugTipText()+": "+model.getDebug()+"\n";
		s = s+ model.degreeTipText()+": "+model.getDegree()+"\n";
		s = s+ model.doNotCheckCapabilitiesTipText()+": "+model.getDoNotCheckCapabilities()+"\n";
		s = s+ model.doNotReplaceMissingValuesTipText()+": "+model.getDoNotReplaceMissingValues()+"\n";
		s = s+ model.epsTipText()+": "+model.getEps()+"\n";
		s = s+ model.gammaTipText()+": "+model.getGamma()+"\n";
		s = s+ model.kernelTypeTipText()+": "+model.getKernelType().toString()+"\n";		
		s = s+ model.lossTipText()+": "+model.getLoss()+"\n";
		s = s+ model.modelFileTipText()+": "+model.getModelFile().toString()+"\n"; 			
		s = s+ model.normalizeTipText()+": "+model.getNormalize()+"\n";
		s = s+ model.nuTipText()+": "+model.getNu()+"\n";
		s = s+ model.probabilityEstimatesTipText()+": "+model.getProbabilityEstimates()+"\n";
		s = s+ model.seedTipText()+": "+model.getSeed()+"\n";
		s = s+ model.shrinkingTipText()+": "+model.getShrinking()+"\n";
		s = s+ model.weightsTipText()+": "+model.getWeights()+"\n";
		

		s = s+"\n=================================\n";
		
		s = s+ "Model global information: "+model.globalInfo()+"\n";
		s = s+ "Model Hash Code: "+ model.hashCode()+"\n";
		s = s+ "Model toString() representation: "+ model.toString()+"\n";
		s = s+ "Model capabilities: "+ model.getCapabilities()+"\n";
		s = s+ "Model Technical information: "+ model.getTechnicalInformation().toString()+"\n";
		s = s+ "Model revision: "+ model.getRevision()+"\n";
		s = s+ "Model absolute path: "+ model.getModelFile().getAbsolutePath()+"\n";
		
		String[] options = model.getOptions();
		String opts = "";
		for (int t=0;t<options.length;t++){opts=opts+" "+options[t];}
		opts.trim();
		
		s = s+ "Model options: "+ opts+"\n";
		s = s+"\n=================================\n";
		
		
		
		
		return s;
	}
	
}
