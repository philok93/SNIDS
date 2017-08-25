package unitTest;

import main.*;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.meta.Vote;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

/**
 * @author Philokypros Ioulianou
 * This class is created with JUnit.It tests the main methods
 * of MethodsIDS.java class.The actual results are compared to the
 * expected results.In case of wrong results it shows an error. 
 */
public class MethodsIDSTest {

	MethodsIDS meth=new MethodsIDS();
	
	/**
	 * Test method for {@link MethodsIDS#getData()}.
	 */
	@Test
	public void testGetData() {
		//Read train data
		Instances n=meth.getData("C:\\Users\\User\\Documents\\University\\KDDTrain+_20Percent.arff", 0);
		//Read test data
		Instances test=meth.getData("C:\\Users\\User\\Documents\\University\\KDDTest+.arff", 1);
		try {
			DataSource source = new DataSource("C:\\Users\\User\\Documents\\University\\KDDTrain+_20Percent.arff");
			Instances n1=source.getDataSet(); n1.setClassIndex(n1.numAttributes() - 1);
			assertEquals(n1.numInstances(), n.numInstances());
			
			DataSource ts = new DataSource("C:\\Users\\User\\Documents\\University\\KDDTest+.arff");
			Instances ts1=ts.getDataSet(); ts1.setClassIndex(ts1.numAttributes() - 1);

			assertEquals(ts1.numInstances(), test.numInstances());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Test method for {@link MethodsIDS#trainHANEval(java.lang.String)}.
	 */
	@Test
	public void testTrainHANEval() {
		//Train classifier
		meth.setOptionsHAN(new ArrayList<String>(0));
		Instances train=meth.getData("C:\\Users\\User\\Documents\\University\\KDDTrain+_20Percent.arff", 0);
		Instances testd=meth.getData("C:\\Users\\User\\Documents\\University\\KDDTest+.arff", 1);
		Evaluation ev=meth.trainHANEval(null,null);
		J48 cl=new J48();
		try {
			cl.buildClassifier(train);
			Evaluation ev1= new Evaluation(train);
			ev1.evaluateModel(cl, testd);
			
			assertEquals(ev1.toSummaryString(),ev.toSummaryString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Test method for {@link MethodsIDS#trainNANEval(java.lang.String)}.
	 */
	@Test
	public void testTrainNANEval() {
		Instances train=meth.getData("C:\\Users\\User\\Documents\\University\\KDDTrain+_20Percent.arff", 0);
		Instances testd=meth.getData("C:\\Users\\User\\Documents\\University\\KDDTest+.arff", 1);
	
		Vote vote=new Vote();
		
		Classifier[] classifiers = {				
				new LibSVM(),
				new RandomForest(),
				new RandomForest()
		};

		vote.setClassifiers(classifiers);
		Evaluation ev=null;
		Evaluation ev2=meth.trainNANEval(null,null);
		try {
			vote.buildClassifier(train);
			ev = new Evaluation(train);
			ev.evaluateModel(vote, testd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(ev.toSummaryString(), ev2.toSummaryString());
		
		
		
	}

}
