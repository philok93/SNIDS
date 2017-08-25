package main;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

/*
 * This class is a JFrame.It shows the NAN-let options. The
 * user can select the parameters for SVM and Random Forest.
 */
public class OptionsNAN extends JFrame {

	private JPanel contentPane;
	private JTextField txtCoef;
	private JTextField txtGamma;
	private JTextField txtCost;
	private JTextField txtMemory;
	private JTextField txtSeed;
	private JTextField txtDegree;
	private JTextField txtTolerance;
	private MainIDS mainids;
	private JTextField txtIterations;
	private JTextField txtAttr;
	private JTextField txtMinLeaf;
	private JTextField txtSeednum;
	private JTextField txtDepth;
	int kernel;
	public ArrayList<String> NANarroptRF=new ArrayList<String>(),NANarroptSVM=new ArrayList<String>();

	/**
	 * Create the frame.
	 */
	public OptionsNAN(MainIDS main) {
		mainids=main;

		setTitle("NAN Options");
		setBounds(100, 100, 860, 388);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNanletOptions = new JLabel("SVM options");
		lblNanletOptions.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNanletOptions.setBounds(111, 11, 89, 21);
		contentPane.add(lblNanletOptions);

		JLabel lblKernelFunctionDegree = new JLabel("Kernel function degree (d):");
		lblKernelFunctionDegree.setBounds(10, 43, 130, 21);
		contentPane.add(lblKernelFunctionDegree);

		JLabel lblNewLabel = new JLabel("Gamma (g):");
		lblNewLabel.setBounds(10, 71, 56, 14);
		contentPane.add(lblNewLabel);

		JLabel lblSetCoefIn = new JLabel("Coef0 in kernel function:");
		lblSetCoefIn.setBounds(10, 94, 123, 14);
		contentPane.add(lblSetCoefIn);

		JLabel lblCostForSet = new JLabel("Cost (C) for C-SVC, epsilon-SVR, and nu-SVR:");
		lblCostForSet.setBounds(10, 119, 221, 21);
		contentPane.add(lblCostForSet);

		JLabel lblParameterNuOf = new JLabel("Set cache memory size in MB:");
		lblParameterNuOf.setBounds(10, 144, 155, 21);
		contentPane.add(lblParameterNuOf);

		JLabel lblSeed = new JLabel("Random seed:");
		lblSeed.setBounds(10, 170, 168, 21);
		contentPane.add(lblSeed);

		txtCoef = new JTextField();
		txtCoef.setText("0");
		txtCoef.setBounds(175, 94, 48, 21);
		contentPane.add(txtCoef);
		txtCoef.setColumns(10);

		txtGamma = new JTextField();
		txtGamma.setText("0.0238");
		txtGamma.setBounds(175, 68, 48, 21);
		contentPane.add(txtGamma);
		txtGamma.setColumns(10);

		txtCost = new JTextField();
		txtCost.setText("1");
		txtCost.setBounds(239, 119, 42, 21);
		contentPane.add(txtCost);
		txtCost.setColumns(10);

		txtMemory = new JTextField();
		txtMemory.setText("40");
		txtMemory.setBounds(175, 144, 48, 20);
		contentPane.add(txtMemory);
		txtMemory.setColumns(10);

		JCheckBox chckbxUseTheShrinking = new JCheckBox("Use the shrinking heuristics");
		chckbxUseTheShrinking.setSelected(true);
		chckbxUseTheShrinking.setBounds(175, 275, 155, 22);
		contentPane.add(chckbxUseTheShrinking);

		JCheckBox chckbxProbEst = new JCheckBox("<html>Generate probability estimates for classification</html>");
		chckbxProbEst.setBounds(10, 300, 168, 37);
		contentPane.add(chckbxProbEst);

		txtSeed = new JTextField();
		txtSeed.setText("1");
		txtSeed.setBounds(175, 170, 48, 21);
		contentPane.add(txtSeed);
		txtSeed.setColumns(10);

		txtDegree = new JTextField();
		txtDegree.setText("3");
		txtDegree.setBounds(175, 43, 48, 20);
		contentPane.add(txtDegree);
		txtDegree.setColumns(10);

		JLabel lblToleranceOfTermination = new JLabel("Tolerance of termination criterion:");
		lblToleranceOfTermination.setBounds(10, 202, 168, 21);
		contentPane.add(lblToleranceOfTermination);

		txtTolerance = new JTextField();
		txtTolerance.setText("0.001");
		txtTolerance.setBounds(175, 203, 49, 25);
		contentPane.add(txtTolerance);
		txtTolerance.setColumns(10);

		JComboBox cmbType = new JComboBox();
		cmbType.setBounds(80, 236, 70, 21);
		contentPane.add(cmbType);
		cmbType.addItem("Linear");
		cmbType.addItem("Polynomial");
		cmbType.addItem("RBF");
		cmbType.addItem("Sigmoid");
		cmbType.setSelectedIndex(2);
		cmbType.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {					
				if (e.getStateChange() == ItemEvent.SELECTED){
					kernel=cmbType.getSelectedIndex();
				}
			}
		});

		JLabel lblKernel = new JLabel("Kernel type:");
		lblKernel.setBounds(10, 236, 60, 21);
		contentPane.add(lblKernel);

		JLabel lblRandomForestOptions = new JLabel("Random Forest options");
		lblRandomForestOptions.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRandomForestOptions.setBounds(488, 9, 168, 25);
		contentPane.add(lblRandomForestOptions);

		JCheckBox chckbxCalculateTheOut = new JCheckBox("Calculate the out of bag error");
		chckbxCalculateTheOut.setBounds(637, 178, 168, 23);
		contentPane.add(chckbxCalculateTheOut);

		JCheckBox chckbxOutputComplexitybasedStatistics = new JCheckBox("<html>Output complexity-based statistics when out-of-bag evaluation is<br>performed</html>");
		chckbxOutputComplexitybasedStatistics.setBounds(637, 253, 201, 57);
		contentPane.add(chckbxOutputComplexitybasedStatistics);

		JCheckBox chckbxPrintTheIndividual = new JCheckBox("Print the individual classifiers in the output");
		chckbxPrintTheIndividual.setBounds(371, 263, 230, 21);
		contentPane.add(chckbxPrintTheIndividual);

		JCheckBox chckbxComputeAndOutput = new JCheckBox("<html>Compute and output<br>attribute importance (mean impurity decrease method)</html>");
		chckbxComputeAndOutput.setBounds(637, 198, 201, 61);
		contentPane.add(chckbxComputeAndOutput);

		txtIterations = new JTextField();
		txtIterations.setText("100");
		txtIterations.setBounds(545, 73, 56, 21);
		contentPane.add(txtIterations);
		txtIterations.setColumns(10);

		JLabel lblNumberOfIterations = new JLabel("Number of iterations:");
		lblNumberOfIterations.setBounds(380, 66, 102, 37);
		contentPane.add(lblNumberOfIterations);

		JCheckBox chckbxExecuteInParallel = new JCheckBox("Execute in parallel");
		chckbxExecuteInParallel.setBounds(637, 140, 113, 34);
		contentPane.add(chckbxExecuteInParallel);

		JLabel lblNumberOfAttributes = new JLabel("<html>Number of attributes<br>to randomly investigate:</html>");
		lblNumberOfAttributes.setBounds(380, 105, 153, 28);
		contentPane.add(lblNumberOfAttributes);

		txtAttr = new JTextField();
		txtAttr.setText("0");
		txtAttr.setBounds(545, 113, 56, 20);
		contentPane.add(txtAttr);
		txtAttr.setColumns(10);

		JLabel lblSetMinimumNumber = new JLabel("<html>Minimum number of<br>instances per leaf:</html>");
		lblSetMinimumNumber.setBounds(380, 131, 97, 57);
		contentPane.add(lblSetMinimumNumber);

		txtMinLeaf = new JTextField();
		txtMinLeaf.setText("1");
		txtMinLeaf.setBounds(545, 153, 56, 20);
		contentPane.add(txtMinLeaf);
		txtMinLeaf.setColumns(10);

		txtSeednum = new JTextField();
		txtSeednum.setText("1");
		txtSeednum.setBounds(545, 195, 56, 20);
		contentPane.add(txtSeednum);
		txtSeednum.setColumns(10);

		JLabel lblSeedForRandom = new JLabel("<html>Seed for random<br>number generator:</html>");
		lblSeedForRandom.setBounds(380, 185, 102, 34);
		contentPane.add(lblSeedForRandom);

		JLabel lblTheMaximumDepth = new JLabel("<html>Max tree depth<br>(0 for unlimited):</html>");
		lblTheMaximumDepth.setBounds(380, 219, 89, 34);
		contentPane.add(lblTheMaximumDepth);

		txtDepth = new JTextField();
		txtDepth.setText("0");
		txtDepth.setBounds(545, 228, 56, 20);
		contentPane.add(txtDepth);
		txtDepth.setColumns(10);

		JCheckBox chckbxBreakTiesRandomly = new JCheckBox("<html>Break ties randomly when<br>several attributes look equally good</html>");
		chckbxBreakTiesRandomly.setBounds(637, 67, 197, 39);
		contentPane.add(chckbxBreakTiesRandomly);

		JCheckBox chckbxDebugMode = new JCheckBox("Debug mode");
		chckbxDebugMode.setBounds(637, 102, 97, 41);
		contentPane.add(chckbxDebugMode);

		JCheckBox chckbxMissing = new JCheckBox("<html>Missing value<br>replacement</html>");
		chckbxMissing.setBounds(10, 264, 97, 37);
		contentPane.add(chckbxMissing);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(343, 0, 15, 310);
		contentPane.add(separator);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NANarroptRF.clear();
				NANarroptSVM.clear();
				setVisible(false);

				kernel=cmbType.getSelectedIndex();

				//Random forest options
				if (NANarroptRF!=null){

					if (chckbxCalculateTheOut.isSelected())
						NANarroptRF.add("-O");
					if (chckbxPrintTheIndividual.isSelected())
						NANarroptRF.add("-print");
					if (chckbxOutputComplexitybasedStatistics.isSelected())
						NANarroptRF.add("-output-out-of-bag-complexity-statistics");
					if (chckbxComputeAndOutput.isSelected())
						NANarroptRF.add("-attribute-importance");
					if (chckbxExecuteInParallel.isSelected()){
						NANarroptRF.add("-num-slots");NANarroptRF.add("0");}
					NANarroptRF.add("-I");NANarroptRF.add(txtIterations.getText());
					NANarroptRF.add("-K");NANarroptRF.add(txtAttr.getText());
					NANarroptRF.add("-M");NANarroptRF.add(txtMinLeaf.getText());
					NANarroptRF.add("-S");NANarroptRF.add(txtSeednum.getText());
					NANarroptRF.add("-depth");NANarroptRF.add(txtDepth.getText());
					if (chckbxBreakTiesRandomly.isSelected())
						NANarroptRF.add("-B");
					if (chckbxDebugMode.isSelected())
						NANarroptRF.add("-output-debug-info");

				}

				//SVM options
				if (NANarroptSVM!=null){
					NANarroptSVM.add("-K");NANarroptSVM.add(String.valueOf(kernel));
					NANarroptSVM.add("-D");NANarroptSVM.add(txtDegree.getText());
					NANarroptSVM.add("-G");NANarroptSVM.add(txtGamma.getText());
					NANarroptSVM.add("-R");NANarroptSVM.add(txtCoef.getText());
					NANarroptSVM.add("-C");NANarroptSVM.add(txtCost.getText());
					NANarroptSVM.add("-M");NANarroptSVM.add(txtMemory.getText());
					if (chckbxMissing.isSelected())
						NANarroptSVM.add("-V");

					if (chckbxUseTheShrinking.isSelected())
						NANarroptSVM.add("-H");
					if (chckbxProbEst.isSelected())
						NANarroptSVM.add("-B");
					NANarroptSVM.add("-E");
					NANarroptSVM.add(txtTolerance.getText());
					NANarroptSVM.add("-seed");
					NANarroptSVM.add(txtSeed.getText());

				}

				mainids.setEnabled(true);
				mainids.requestFocus();

			}
		});
		btnSave.setBounds(264, 320, 69, 23);
		contentPane.add(btnSave);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmbType.setSelectedIndex(2);
				chckbxUseTheShrinking.setSelected(true);
				chckbxMissing.setSelected(false);
				chckbxProbEst.setSelected(false);
				chckbxPrintTheIndividual.setSelected(false);
				chckbxBreakTiesRandomly.setSelected(false);
				chckbxCalculateTheOut.setSelected(false);
				chckbxComputeAndOutput.setSelected(false);
				chckbxDebugMode.setSelected(false);
				chckbxExecuteInParallel.setSelected(false);
				chckbxOutputComplexitybasedStatistics.setSelected(false);
				txtDegree.setText("3");
				txtAttr.setText("0");
				txtCoef.setText("0");
				txtCost.setText("1");
				txtDepth.setText("0");
				txtGamma.setText("0.0238");
				txtIterations.setText("100");
				txtMemory.setText("40");
				txtMinLeaf.setText("1");
				txtSeed.setText("1");
				txtSeednum.setText("1");
				txtTolerance.setText("0.001");
			}
		});
		btnReset.setBounds(343, 321, 60, 23);
		contentPane.add(btnReset);

		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
				mainids.setEnabled(true);
				mainids.requestFocus();
			}
			
		});

	}
}
