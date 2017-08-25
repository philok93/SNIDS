package main;
import java.awt.BorderLayout;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

import weka.filters.supervised.attribute.AttributeSelection;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.WrapperSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.evaluation.output.prediction.PlainText;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.meta.Vote;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;

public class MethodsIDS {

	static int chkEnabled=0,useFilter=1,visual=0;
	private Instances traindata,testdata;
	private ArrayList<String> optionsHAN=new ArrayList<String>();
	String[] opRF=null,optVote=null,opSVM=null;
	int wrapflag=0;

	/*
	 * Get HAN-let options
	 */
	public ArrayList<String> getOptionsHAN() {
		return optionsHAN;
	}

	/*
	 * Set HAN-let options
	 */
	public void setOptionsHAN(ArrayList<String> optionsHAN) {
		this.optionsHAN = optionsHAN;
	}

	/*
	 * Get NAN-let options
	 */
	public ArrayList<String> getOptionsNAN() {
		ArrayList<String> opts=new ArrayList<>();
		for (int i = 0; i < opRF.length; i++) {
			opts.add(opRF[i]);
		}
		for (int i = 0; i < opSVM.length; i++) {
			opts.add(opSVM[i]);
		}
		return opts;
	}

	/*
	 * Set NAN-let options: SVM, RF, Vote classifiers
	 */
	public void setOptionsNAN(ArrayList<String> optsVote,ArrayList<String> optSVM,ArrayList<String> optRF) {
		if (optsVote!=null){
			optVote=new String[optsVote.size()];
			for (int i = 0; i < optsVote.size(); i++) {
				System.out.println(optsVote.get(i));
				optVote[i]=optsVote.get(i);
			}

		}
		if (optRF!=null){
			opRF=new String[optRF.size()];
			for (int i = 0; i < optRF.size(); i++) {
				System.out.println(optRF.get(i));
				opRF[i]=optRF.get(i);
			}
		}

		if (optSVM!=null){
			opSVM=new String[optSVM.size()];
			for (int i = 0; i < optSVM.size(); i++) {
				System.out.println("rr:"+optSVM.get(i));
				opSVM[i]=optSVM.get(i);
			}
		}

	}

	/*
	 * Read datasets from file
	 */
	public Instances getData(String file,int flag){
		DataSource source;

		try {
			source = new DataSource(file);
			if (flag==0)
				traindata=source.getDataSet();
			else
				testdata=source.getDataSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (flag==0){
			traindata.setClassIndex(traindata.numAttributes() - 1);
			return traindata;
		}else{
			testdata.setClassIndex(testdata.numAttributes() - 1);
			return testdata;
		}
	}

	public Evaluation trainHANEval(String file, J48 impJ){
		String[] options=new String[optionsHAN.size()];
		Evaluation evaluation = null;
		J48 tree=null;
		if (impJ==null)
			tree = new J48();         // new instance of tree
		else
			tree=impJ;

		try {
			AttributeSelectedClassifier clsfier = null;
			CfsSubsetEval cfs=null;
			WrapperSubsetEval wrpSub=null;
			BestFirst bf=null;
			InputMappedClassifier mapped = new InputMappedClassifier();

			//Skip training if model imported
			if (impJ==null){

				for (int i = 0; i < optionsHAN.size(); i++) {
					System.out.println(optionsHAN.get(i));
					options[i]=optionsHAN.get(i);
				}	  
				tree.setOptions(options);

				if (chkEnabled==0){
					long startTime = System.currentTimeMillis();
					tree.buildClassifier(traindata); //train classifier
					long endTime = System.currentTimeMillis();
					long total = endTime - startTime;
					System.out.println("Time to build model:"+(total/1000.0)+" seconds");
					if (OptionsGen.saveModel!=null)
						weka.core.SerializationHelper.write(OptionsGen.saveModel, tree);
				}
				else{
					clsfier= new AttributeSelectedClassifier();
					bf=new BestFirst();

					//Filter is set
					if (useFilter==1){
						cfs= new CfsSubsetEval();
						clsfier.setClassifier(tree);
						clsfier.setEvaluator(cfs);
						clsfier.setSearch(bf);

						long startTime=System.currentTimeMillis();
						clsfier.buildClassifier(traindata);
						long endTime=System.currentTimeMillis();
						long total = endTime - startTime;
						System.out.println("Time to build model:"+(total/1000.0)+" seconds");
						if (OptionsGen.saveModel!=null)
							weka.core.SerializationHelper.write(OptionsGen.saveModel, clsfier);
					}else if (useFilter==0){
						//Wrapper filter set with J48 evaluator
						wrapflag=1;
						String[] opt={"-B","weka.classifiers.trees.J48"};

						wrpSub= new WrapperSubsetEval();
						AttributeSelection sel = new AttributeSelection();  // package weka.filters.supervised.attribute!
						wrpSub.setOptions(opt);
						sel.setEvaluator(wrpSub);
						sel.setSearch(bf);
						sel.setInputFormat(traindata);
						// generate new data
						Instances newData = Filter.useFilter(traindata,sel);
						newData.toSummaryString();

						long startTime = System.currentTimeMillis();
						tree.buildClassifier(newData);
						long endTime = System.currentTimeMillis();
						long total = endTime - startTime;
						System.out.println("Time to build model:"+(total/1000.0)+" seconds");

						mapped.setModelHeader(newData);

						mapped.setClassifier(tree);
						if (OptionsGen.saveModel!=null)
							weka.core.SerializationHelper.write(OptionsGen.saveModel, mapped);
					}
				}

			}

			evaluation = new Evaluation(traindata);
			PlainText predictionOutput =null;
			Boolean outputDistributions =null;

			//Export predictions to file
			if (MainIDS.expFile==1){
				StringBuffer predictionSB = new StringBuffer();
				outputDistributions=new Boolean(true);

				predictionOutput= new PlainText();
				predictionOutput.setBuffer(predictionSB);
				predictionOutput.setOutputDistribution(false);
				predictionOutput.setHeader(new Instances(traindata,0));

			}

			//Cases for exporting into file and use or not a test set
			long startTime = System.currentTimeMillis();
			if (MainIDS.useTestSet==1 && MainIDS.expFile==1 && chkEnabled==0){
				predictionOutput.printHeader();
				evaluation.evaluateModel(tree, testdata,predictionOutput,outputDistributions);

			} else if (MainIDS.useTestSet==1 && MainIDS.expFile==0 && chkEnabled==0){
				evaluation.evaluateModel(tree, testdata);
			}else if (MainIDS.useTestSet==0 && MainIDS.expFile==1 && chkEnabled==0){
				evaluation.crossValidateModel(tree, traindata, OptionsGen.folds, new Random(1), predictionOutput,outputDistributions);

			}else if (MainIDS.useTestSet==0 && MainIDS.expFile==0 && chkEnabled==0){
				evaluation.crossValidateModel(tree, traindata, OptionsGen.folds, new Random(1));

			}else if (MainIDS.useTestSet==1 && MainIDS.expFile==0 && (chkEnabled==1 || wrapflag==1)){
				if (wrapflag==1)
					evaluation.evaluateModel(mapped, testdata);
				else
					evaluation.evaluateModel(clsfier, testdata);
			}else if (MainIDS.useTestSet==1 && MainIDS.expFile==1 && (chkEnabled==1 || wrapflag==1)){
				predictionOutput.printHeader();
				if (wrapflag==1)
					evaluation.evaluateModel(mapped, testdata,predictionOutput,outputDistributions);
				else
					evaluation.evaluateModel(clsfier, testdata,predictionOutput,outputDistributions);
			}else if (MainIDS.useTestSet==0 && MainIDS.expFile==1 && (chkEnabled==1 || wrapflag==1)){
				if (wrapflag==1)
					evaluation.crossValidateModel(mapped, traindata, OptionsGen.folds, new Random(1),predictionOutput,outputDistributions);
				else
					evaluation.crossValidateModel(clsfier, traindata, OptionsGen.folds, new Random(1),predictionOutput,outputDistributions);

			}else if (MainIDS.useTestSet==0 && MainIDS.expFile==0 && (chkEnabled==1 || wrapflag==1)){
				if (wrapflag==1)
					evaluation.crossValidateModel(mapped, traindata, OptionsGen.folds, new Random(1));
				else
					evaluation.crossValidateModel(clsfier, traindata, OptionsGen.folds, new Random(1));
			}
			long endTime = System.currentTimeMillis();
			long total = endTime - startTime;
			System.out.println("Time to test the model:"+(total/1000.0)+" seconds");
			wrapflag=0;
			System.out.println(evaluation.toSummaryString("\nResults\n======\n", false));
			System.out.println(evaluation.toClassDetailsString());
			System.out.println(evaluation.toMatrixString());
			if (MainIDS.expFile==1 && file!=null){
				try(FileWriter fw = new FileWriter(file)) {
					fw.write(String.valueOf(predictionOutput.getBuffer()));

				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			//Display classifier
			if (visual==1){
				final javax.swing.JFrame frame=new javax.swing.JFrame("J48 Classifier Tree Visualizer");
				frame.setSize(1200,700);
				frame.getContentPane().setLayout(new BorderLayout());
				TreeVisualizer tv = new TreeVisualizer(null,
						tree.graph(),new PlaceNode2());
				frame.getContentPane().add(tv, BorderLayout.CENTER);
				frame.setVisible(true);
				tv.fitToScreen();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return evaluation;
	}

	//Method for executing NAN-let (SVM and Random Forest)
	public Evaluation trainNANEval(String file,Vote impVote){
		Evaluation evaluation = null;

		Vote vote=null;
		if (impVote==null)
			vote=new Vote();
		else
			vote=impVote;

		try {		  
			AttributeSelectedClassifier clsfier =new AttributeSelectedClassifier();
			CfsSubsetEval cfs=new CfsSubsetEval();
			InputMappedClassifier mapped = new InputMappedClassifier();
			WrapperSubsetEval wsub=new WrapperSubsetEval();
			BestFirst bf=new BestFirst();

			PlainText predictionOutput =null;
			Boolean outputDistributions =null;

			Instances newData = null;

			//Skip training if model imported
			if (impVote==null){

				//Export predictions to file
				if (MainIDS.expFile==1){
					StringBuffer predictionSB = new StringBuffer();
					outputDistributions=new Boolean(true);

					predictionOutput= new PlainText();
					predictionOutput.setBuffer(predictionSB);
					predictionOutput.setOutputDistribution(false);

				}

				//Add SVM,Random Forest classifiers.
				//Two instances of Random Forest means weighted vote.
				Classifier[] classifiers = {				
						new LibSVM(),
						new RandomForest(),
						new RandomForest()
				};

				//Set the options for each classifier
				if (opSVM!=null && opSVM.length>0)
					((LibSVM)classifiers[0]).setOptions(opSVM);
				if (opRF!=null && opRF.length>0)
					((RandomForest)classifiers[1]).setOptions(opRF);
				if (optVote!=null && optVote.length>0)
					vote.setOptions(optVote);

				vote.setClassifiers(classifiers);

				//Train the right classifier
				if (chkEnabled==0){
					if (MainIDS.expFile==1)
						predictionOutput.setHeader(new Instances(traindata,0));
					long startTime = System.currentTimeMillis();
					vote.buildClassifier(traindata);
					long endtime = System.currentTimeMillis();
					long total = endtime - startTime;
					System.out.println("Time to build model:"+(total/1000.0)+" seconds");
					if (OptionsGen.saveModel!=null)
						weka.core.SerializationHelper.write(OptionsGen.saveModel, vote);
				}else if (useFilter==1){
					//Filter is set
					clsfier.setClassifier(vote);
					clsfier.setEvaluator(cfs);
					clsfier.setSearch(bf);
					long startTime = System.currentTimeMillis();
					clsfier.buildClassifier(traindata);
					long endtime = System.currentTimeMillis();
					long total = endtime - startTime;
					System.out.println("Time to build model:"+(total/1000.0)+" seconds");
					if (OptionsGen.saveModel!=null)
						weka.core.SerializationHelper.write(OptionsGen.saveModel, clsfier);
				}else if (useFilter==0){
					//Wrapper filter is set.
					wrapflag=1;
					String[] opt={"-B","weka.classifiers.trees.J48"};

					wsub= new WrapperSubsetEval();
					AttributeSelection sel = new AttributeSelection();  // package weka.filters.supervised.attribute!
					wsub.setOptions(opt);
					sel.setEvaluator(wsub);
					sel.setSearch(bf);
					sel.setInputFormat(traindata);
					//Generate new data
					newData = Filter.useFilter(traindata,sel);
					newData.toSummaryString();

					long startTime = System.currentTimeMillis();
					vote.buildClassifier(newData);
					long endTime   = System.currentTimeMillis();

					long total = endTime - startTime;
					System.out.println("Time to build model:"+(total/1000.0)+" seconds");

					mapped.setModelHeader(newData);

					mapped.setClassifier(vote);
					evaluation = new Evaluation(newData);
					//Change header of output file
					if (MainIDS.expFile==1)
						predictionOutput.setHeader(new Instances(newData,0));

					if (OptionsGen.saveModel!=null)
						weka.core.SerializationHelper.write(OptionsGen.saveModel, mapped);
				}

			}//impVote is not null

			if (chkEnabled==0 || useFilter==1)
				evaluation = new Evaluation(traindata);

			long startTime = System.currentTimeMillis();
			//We have 3 parameters:use test set, use feature selection, and export to file.
			if (MainIDS.useTestSet==1 && (chkEnabled==1 || wrapflag==1) && MainIDS.expFile==0){
				if (wrapflag==1)
					evaluation.evaluateModel(mapped, testdata);
				else
					evaluation.evaluateModel(clsfier, testdata);
			}else if (MainIDS.useTestSet==1 && (chkEnabled==1 || wrapflag==1) && MainIDS.expFile==1){
				predictionOutput.printHeader();
				if (wrapflag==1)
					evaluation.evaluateModel(mapped, testdata,predictionOutput,outputDistributions);
				else
					evaluation.evaluateModel(clsfier, testdata,predictionOutput,outputDistributions);
			}else if (MainIDS.useTestSet==1 && chkEnabled==0 && MainIDS.expFile==0)
				evaluation.evaluateModel(vote, testdata);
			else if (MainIDS.useTestSet==1 && chkEnabled==0 && MainIDS.expFile==1){
				predictionOutput.printHeader();
				evaluation.evaluateModel(vote, testdata,predictionOutput,outputDistributions);
			}else if (MainIDS.useTestSet==0 && chkEnabled==0 && MainIDS.expFile==0)
				evaluation.crossValidateModel(vote, traindata,  OptionsGen.folds, new Random(1));
			else if (MainIDS.useTestSet==0 && chkEnabled==0 && MainIDS.expFile==1)
				evaluation.crossValidateModel(vote, traindata,  OptionsGen.folds, new Random(1),predictionOutput,outputDistributions);
			else if (MainIDS.useTestSet==0 && (chkEnabled==1 || wrapflag==1) && MainIDS.expFile==1){
				if (wrapflag==1)
					evaluation.crossValidateModel(mapped, newData,  OptionsGen.folds, new Random(1),predictionOutput,outputDistributions);
				else
					evaluation.crossValidateModel(clsfier, traindata,  OptionsGen.folds, new Random(1),predictionOutput,outputDistributions);
			}else{	
				if (wrapflag==1)
					evaluation.crossValidateModel(mapped, newData,  OptionsGen.folds, new Random(1));
				else
					evaluation.crossValidateModel(clsfier, traindata,  OptionsGen.folds, new Random(1));
			}
			long endTime = System.currentTimeMillis();
			long total = endTime - startTime;
			System.out.println("Time to test the model:"+(total/1000.0)+" seconds");

			wrapflag=0;
			System.out.println(evaluation.toSummaryString("\nResults\n==========\n",false));
			System.out.println(evaluation.toClassDetailsString());
			System.out.println(evaluation.toMatrixString());
			if (MainIDS.expFile==1 && file!=null){
				try(FileWriter fw = new FileWriter(file)) {
					fw.write(String.valueOf(predictionOutput.getBuffer()));

				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return evaluation;
	}


}
