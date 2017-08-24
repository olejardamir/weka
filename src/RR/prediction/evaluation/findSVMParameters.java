package RR.prediction.evaluation;
/**
 * Still in experimental stage, to be used to fine-tune the SVM parameters.
 * However, this is just too slow since the brute-force is applied.
 * @author Damir Olejar
 */
import weka.classifiers.Evaluation;
import RR.prediction.classifiers.SVM.SVM;

public class findSVMParameters {

	private SVM svm = new SVM();
	private EvaluateModelSVM eval;

	
	public findSVMParameters(){}
	
 
	
	public String getNextOption(){
	if (SVMType == -1){return "END OF SEARCH";}
	String option = "weka.classifiers.functions.LibSVM";	
	
	seed =  seed+seedStep;
	if ( seed >  seedMax){
		seed = seedMin;
		probEstimate++;
			if (probEstimate>1){
				probEstimate = 0;
				weights = weights+weightsStep;
				if (weights>weightsMax){
					weights=weightsMin;
					dontReplaceMissing++;
					if (dontReplaceMissing>1){
						dontReplaceMissing=0;
						normalize++;
						if (normalize>1){
							normalize=0;
							shrinking++;
							if (shrinking>1){
								shrinking=0;
								loss = loss+lossStep;
								if (loss>lossMax){
									loss = lossMin;
									eps = eps+epsStep;
									if (eps>epsMax){
										eps=epsMin;
										cost = cost+costStep;
										if(cost>costMax){
											cost=costMin;
											nu = nu+nuStep;
											if (nu>nuMax){
												nu=nuMin;
												coef0=coef0+coef0Step;
												if (coef0>coef0Max){
													coef0=coef0Min;
													gamma=gamma+gammaStep;
													if(gamma>gammaMax){
														gamma=gammaMin;
														degree=degree+degreeStep;
														if(degree>degreeMax){
															degree=degreeMin;
															kernelType ++;
															if (kernelType>3){
																kernelType=0;
																SVMType++;
																if (SVMType>4){
																	return "END OF SEARCH";
																}
															}
															
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					
				}
			}
 	
			
	}
	
	if (SVMType == -1){return "END OF SEARCH";}
	
	//don't replace missing
	String dnrp = "";
	if (dontReplaceMissing==1){dnrp = " -V";}
	
	//normalize
	String nrmlz = "";
	if (normalize==1){nrmlz=" -Z";}
	
	//shrinking
	String shrk = "";
	if (shrinking==0){shrk=" -H";}
	
	//probability estimates
	String pest = "";
	if (probEstimate==1){pest=" -B";}
		
	
	//EXAMPLE
	//weka.classifiers.functions.LibSVM -S 3 -K 1 -D 5 -G 1.0 -R 1.0 -N 0.5 -M 40.0 -C 3.0 -E 0.001 -P 0.12 -H -Z -V -W 123.0 -B -model -seed 321	
	//weka.classifiers.functions.LibSVM -S 3.0 -K 3.0 -D 0 -G 0.1 -R -100.0 N 0.6 -M 400.0 -C 0.1 -E 0.1 -P -100.0 -W 100.0 -model -seed 1
	
	option = option +" -S "+(int)SVMType+" -K "+(int)kernelType+" -D "+(int)degree+" -G "+gamma+" -R "+coef0+" N "+nu+" -M "+casheSize+" -C "+cost+
			" -E "+eps+" -P "+loss+shrk+nrmlz+dnrp+" -W "+weights+pest+" -model"+" -seed "+(int)seed;
	return option;
		
	}
	
	
	public void searchBestOptions(String trainingCSV, String evaluatingCSV){
		try{
		String a="";
		svm.loadCSV(trainingCSV);
		while (!a.equals("END OF SEARCH")){
			a = getNextOption();
			if (a.equals("END OF SEARCH")) break;
			svm.buildModel(a);
			this.eval = new EvaluateModelSVM(evaluatingCSV, svm.getModel());
			System.out.println(eval.toSummaryString());
			double correlation = eval.getCorrelationCoefficient();
			double error = eval.getMaximumError();
			double ser = eval.getStandardErrorOfRegression();
			
			if (ser<this.SER){this.SER=ser; this.bestSER=a+"<"+"Correlation: "+correlation+", SER: "+ser+" MaximumError: "+error+">";}
			if (error<this.MaxError){this.MaxError=error; this.bestMaxError=a+"<"+"Correlation: "+correlation+", SER: "+ser+" MaximumError: "+error+">";}
			if (correlation>this.Corr){this.Corr=correlation; this.bestCorr=a+"<"+"Correlation: "+correlation+", SER: "+ser+" MaximumError: "+error+">";}
			
			System.out.println("Correlation..."+this.bestCorr);
			System.out.println("SER..."+this.bestSER);
			System.out.println("Error..."+this.bestMaxError);

		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private double SVMType = 3; //3,4
	private double casheSize = 400; //NO EFFECT! should not be changed unless manually
	private double coef0 = 0; //NO EFFECT ??
	private double cost = 0.1; // STEP +6 ; >0
	private int degree = 0; //NO EFFECT ! >=0
	private double eps = 0.1;// ; >0 //NO EFFECT ??
	private double gamma = 0.1; // ; >0 //NO EFFECT ??
	private double kernelType = 0;
	private double loss = 0.1; // <>0 //NO EFFECT ??
	private int normalize = 0;
	private double nu = 0.1;// >0 <=1 //NO EFFECT ??
	private int probEstimate = 0;
	private int seed = 1; //NO EFFECT ??
	private int shrinking = 0;
	private double weights = 0; //NO EFFECT ?? <=>0
	private int dontReplaceMissing = 0;
	
	//TODO: find accepted min vals.
	private double coef0Min = -100;
	private double costMin = 0.1;
	private int degreeMin = 0; //
	private double epsMin = 0.1;
	private double gammaMin = 0.1;
	private double lossMin = 0.1;
	private double nuMin = 0.1;
	private int seedMin = 1;
	private double weightsMin = 0;//
 	
	//TODO: find accepted max vals.
	private double coef0Max = 100;
	private double costMax = 100;
	private int degreeMax = 100;
	private double epsMax = 100;
	private double gammaMax = 100;
	private double lossMax = 100;
	private double nuMax = 1;
	private int seedMax = 1;
	private double weightsMax = 100;

	
	//TODO: find accepted max vals.
	private double coef0Step = 50;
	private double costStep = 50;
	private int degreeStep = 50;
	private double epsStep = 50;
	private double gammaStep = 50;
	private double lossStep = 50;
	private double nuStep = 0.5;
	private int seedStep = 1;
	private double weightsStep = 50;
	
	private String bestMaxError = "NaN";
	private String bestCorr = "NaN";
	private String bestSER = "NaN";
	
	private double MaxError = Double.MAX_VALUE;
	private double Corr = Double.MIN_VALUE;
	private double SER = Double.MAX_VALUE;

	
	/*
	 * SETTERS
	 * 
	 */
	public void setcoef0Min(double change) { this.coef0Min = change; }
	public void setcostMin(double change) { this.costMin = change; }
	public void setdegreeMin(int change) { this.degreeMin = change; }
	public void setepsMin(double change) { this.epsMin = change; }
	public void setgammaMin(double change) { this.gammaMin = change; }
	public void setlossMin(double change) { this.lossMin = change; }
	public void setnuMin(double change) { this.nuMin = change; }
	public void setseedMin(int change) { this.seedMin = change; }
	public void setweightsMin(double change) { this.weightsMin = change; }
	public void setcoef0Max(double change) { this.coef0Max = change; }
	public void setcostMax(double change) { this.costMax = change; }
	public void setdegreeMax(int change) { this.degreeMax = change; }
	public void setepsMax(double change) { this.epsMax = change; }
	public void setgammaMax(double change) { this.gammaMax = change; }
	public void setlossMax(double change) { this.lossMax = change; }
	public void setnuMax(double change) { this.nuMax = change; }
	public void setseedMax(int change) { this.seedMax = change; }
	public void setweightsMax(double change) { this.weightsMax = change; }
	public void setcoef0Step(double change) { this.coef0Step = change; }
	public void setcostStep(double change) { this.costStep = change; }
	public void setdegreeStep(int change) { this.degreeStep = change; }
	public void setepsStep(double change) { this.epsStep = change; }
	public void setgammaStep(double change) { this.gammaStep = change; }
	public void setlossStep(double change) { this.lossStep = change; }
	public void setnuStep(double change) { this.nuStep = change; }
	public void setseedStep(int change) { this.seedStep = change; }
	public void setweightsStep(double change) { this.weightsStep = change; }
	
	
}
