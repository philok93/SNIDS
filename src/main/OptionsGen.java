package main;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import weka.classifiers.meta.Vote;
import weka.classifiers.trees.J48;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

/*
 * The class implements JFrame.It shows the available
 * options of SNIDS.User can select various parameters
 * such as Load/Save model,choose Vote method,use 
 * filter or not and choose test method.
 */
public class OptionsGen extends JFrame {

	private JPanel contentPane;
	private final JLabel lblTesting = new JLabel("Evaluation options:");
	private JTextField txtFolds;
	public static int folds=10;
	private MainIDS mainids;
	public static String saveModel=null;
	private int flagentered=0;
	private String validFile=null;
	private Set<String> buttonState=new HashSet<String>();
	Object ob=null;

	/**
	 * Create the frame.
	 */
	public OptionsGen(MainIDS main) {
		mainids=main;
		setTitle("General Options");
		setBounds(100, 100, 458, 360);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		lblTesting.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTesting.setBounds(10, 44, 109, 23);
		contentPane.add(lblTesting);

		JRadioButton rdbtnUseTestingSet = new JRadioButton("Use testing set");
		rdbtnUseTestingSet.setSelected(true);
		rdbtnUseTestingSet.setBounds(10, 65, 109, 23);
		contentPane.add(rdbtnUseTestingSet);


		JRadioButton rdbtnUseCrossvalidation = new JRadioButton("Use cross-validation");
		rdbtnUseCrossvalidation.setBounds(10, 91, 147, 23);
		contentPane.add(rdbtnUseCrossvalidation);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnUseTestingSet);
		group.add(rdbtnUseCrossvalidation);	

		JLabel lblFolds = new JLabel("<html>Folds for<br>cross-validation:</html>");
		lblFolds.setBounds(10, 121, 91, 29);
		lblFolds.setVisible(false);
		contentPane.add(lblFolds);

		txtFolds = new JTextField();
		txtFolds.setText("10");
		txtFolds.setBounds(96, 130, 36, 20);
		txtFolds.setVisible(false);
		contentPane.add(txtFolds);
		txtFolds.setColumns(10);

		rdbtnUseTestingSet.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (flagentered==1 && rdbtnUseTestingSet.isSelected())
					buttonState.add("rdbtnUseTestingSet");
				else if (buttonState.contains("rdbtnUseTestingSet"))
					buttonState.remove("rdbtnUseTestingSet");
					
				if (rdbtnUseTestingSet.isSelected()){
					lblFolds.setVisible(false);
					txtFolds.setVisible(false);
					MainIDS.useTestSet=1;

				}

			}
		});

		rdbtnUseCrossvalidation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (flagentered==1 && rdbtnUseCrossvalidation.isSelected())
					buttonState.add("rdbtnUseCrossvalidation");
				else if (buttonState.contains("rdbtnUseCrossvalidation"))
					buttonState.remove("rdbtnUseCrossvalidation");
				
				if (rdbtnUseCrossvalidation.isSelected()){
					lblFolds.setVisible(true);
					txtFolds.setVisible(true);
					MainIDS.useTestSet=0;
					folds=Integer.parseInt(txtFolds.getText());
				}
			}
		});

		JLabel lblGeneralOptions = new JLabel("Settings");
		lblGeneralOptions.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblGeneralOptions.setBounds(166, 11, 69, 23);
		contentPane.add(lblGeneralOptions);

		JLabel lblVoteOptions = new JLabel("Vote options:");
		lblVoteOptions.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblVoteOptions.setBounds(241, 44, 91, 23);
		contentPane.add(lblVoteOptions);

		JLabel lblCombinationRuleTo = new JLabel("<html>Combination rule to use in NAN-let:</html>");
		lblCombinationRuleTo.setBounds(241, 62, 128, 42);
		contentPane.add(lblCombinationRuleTo);

		JComboBox comboBox = new JComboBox();

		comboBox.setBounds(241, 109, 147, 29);
		comboBox.addItem("Average of Probabilities");
		comboBox.addItem("Product of Probabilities");
		comboBox.addItem("Majority Voting");
		comboBox.addItem("Minimum Probability");
		comboBox.addItem("Maximum Probability");
		comboBox.setSelectedIndex(0);
		contentPane.add(comboBox);
		
		JRadioButton rdbtnFilterMethod = new JRadioButton("Filter method");
		rdbtnFilterMethod.setBounds(269, 205, 109, 23);
		rdbtnFilterMethod.setVisible(false);
		rdbtnFilterMethod.setSelected(true);
		contentPane.add(rdbtnFilterMethod);
		rdbtnFilterMethod.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (flagentered==1 && rdbtnFilterMethod.isSelected())
					buttonState.add("rdbtnFilterMethod");
				else if (buttonState.contains("rdbtnFilterMethod"))
					buttonState.remove("rdbtnFilterMethod");
				// TODO Auto-generated method stub
				if (rdbtnFilterMethod.isSelected())
					MethodsIDS.useFilter=1;
				else
					MethodsIDS.useFilter=0;

			}
		});
		
		JRadioButton rdbtnWrapperMethod = new JRadioButton("Wrapper method with J48");
		rdbtnWrapperMethod.setBounds(269, 231, 159, 23);
		rdbtnWrapperMethod.setVisible(false);
		contentPane.add(rdbtnWrapperMethod);
		rdbtnWrapperMethod.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (flagentered==1 && rdbtnWrapperMethod.isSelected())
					buttonState.add("rdbtnWrapperMethod");
				else if (buttonState.contains("rdbtnWrapperMethod"))
					buttonState.remove("rdbtnWrapperMethod");
				if (rdbtnWrapperMethod.isSelected())
					MethodsIDS.useFilter=0;
				else
					MethodsIDS.useFilter=1;
			}
		});

		ButtonGroup group2 = new ButtonGroup();
		group2.add(rdbtnFilterMethod);
		group2.add(rdbtnWrapperMethod);	
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Enable Feature selection");
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.BOLD, 11));
		chckbxNewCheckBox.setBounds(250, 181, 163, 23);
		contentPane.add(chckbxNewCheckBox);
		
		JRadioButton rdbtnSaveTrainedModel = new JRadioButton("Save trained model");
		rdbtnSaveTrainedModel.setSelected(true);
		rdbtnSaveTrainedModel.setVisible(false);
		rdbtnSaveTrainedModel.setBounds(10, 205, 122, 23);
		contentPane.add(rdbtnSaveTrainedModel);
		
		JRadioButton rdbtnLoadTrainedModel = new JRadioButton("Load trained model");
		rdbtnLoadTrainedModel.setVisible(false);
		rdbtnLoadTrainedModel.setBounds(10, 256, 122, 23);
		contentPane.add(rdbtnLoadTrainedModel);
		
		
		ButtonGroup g2=new ButtonGroup();
		g2.add(rdbtnLoadTrainedModel);
		g2.add(rdbtnSaveTrainedModel);	
		
		JLabel lblModel = new JLabel("");
		lblModel.setBounds(82, 283, 54, 20);
		contentPane.add(lblModel);
		
		JLabel lblSaveFile = new JLabel("");
		lblSaveFile.setBounds(82, 234, 54, 20);
		contentPane.add(lblSaveFile);
		
		//Object ob=null;
		JButton btnBrowse2 = new JButton("Browse");
		btnBrowse2.setVisible(false);
		btnBrowse2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnLoadTrainedModel.isSelected()){
					
					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"HAN or NAN model files only ", "model");
					fileChooser.setFileFilter(filter);
					if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						lblModel.setText(fileChooser.getSelectedFile().getName()+".model");
						try {
							ob=weka.core.SerializationHelper.read(file.toString());
							if (ob.getClass().getName().matches("weka.classifiers.trees.J48"))
								validFile="J48";
							else if (ob.getClass().getName().matches("weka.classifiers.meta.Vote"))
								validFile="Vote";
							else{
								validFile=null;
								JOptionPane.showMessageDialog(new JFrame(),"Not a valid model file.Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(new JFrame(),"Please choose a valid file.", "Error", JOptionPane.ERROR_MESSAGE);
							lblModel.setText("");
							e.printStackTrace();
						}
			
						
					}
				}else{
					main.impclsfier=null;
					lblModel.setText("Not selected");
				}
					
			}
		});
		btnBrowse2.setBounds(10, 280, 67, 23);
		contentPane.add(btnBrowse2);
		
		JButton btnBrowse1 = new JButton("Browse");
		btnBrowse1.setVisible(false);
		btnBrowse1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(null);
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"HAN and NAN model files ","model");
				chooser.setFileFilter(filter);
				if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
					saveModel=chooser.getSelectedFile()+".model";
					lblSaveFile.setText(chooser.getSelectedFile().getName()+".model");	
											
				}else{
					saveModel=null;
					lblSaveFile.setText("Not selected");
				}
			}
		});
		btnBrowse1.setBounds(10, 231, 67, 23);
		contentPane.add(btnBrowse1);
		
		
		rdbtnSaveTrainedModel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (flagentered==1 && rdbtnSaveTrainedModel.isSelected())
					buttonState.add("rdbtnSaveTrainedModel");
				else if (buttonState.contains("rdbtnSaveTrainedModel"))
					buttonState.remove("rdbtnSaveTrainedModel");
				
				if (rdbtnSaveTrainedModel.isSelected()){
					btnBrowse2.setVisible(false);
					lblModel.setVisible(false);
					btnBrowse1.setVisible(true);
					lblSaveFile.setVisible(true);
					mainids.impclsfier=null;
					validFile=null;
					lblModel.setText("");
					chckbxNewCheckBox.setEnabled(true);
				}
			}
		});
		
		rdbtnLoadTrainedModel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (flagentered==1 && rdbtnLoadTrainedModel.isSelected())
					buttonState.add("rdbtnLoadTrainedModel");
				else if (buttonState.contains("rdbtnLoadTrainedModel"))
					buttonState.remove("rdbtnLoadTrainedModel");
				
			if (rdbtnLoadTrainedModel.isSelected()){
				btnBrowse2.setVisible(true);
				lblModel.setVisible(true);
				btnBrowse1.setVisible(false);
				lblSaveFile.setVisible(false);
				saveModel=null;
				lblSaveFile.setText("");
				chckbxNewCheckBox.setSelected(false);
				chckbxNewCheckBox.setEnabled(false);
				rdbtnFilterMethod.setVisible(false);
				rdbtnWrapperMethod.setVisible(false);
			}				
			
			
			}
		});

		chckbxNewCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (flagentered==1 )
					buttonState.add("chckbxNewCheckBox");
				else if (buttonState.contains("chckbxNewCheckBox"))
					buttonState.remove("chckbxNewCheckBox");
						
				if (chckbxNewCheckBox.isSelected()){
					MethodsIDS.chkEnabled=1;
					rdbtnWrapperMethod.setVisible(true);
					rdbtnFilterMethod.setVisible(true);
				}else{
					MethodsIDS.chkEnabled=0;
					rdbtnWrapperMethod.setVisible(false);
					rdbtnFilterMethod.setVisible(false);
				}
			}
		});
		
		JCheckBox chckbxSaveloadModel = new JCheckBox("Save/Load model");
		chckbxSaveloadModel.setFont(new Font("Tahoma", Font.BOLD, 11));
		chckbxSaveloadModel.setBounds(10, 181, 128, 23);
		contentPane.add(chckbxSaveloadModel);
		
		chckbxSaveloadModel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (flagentered==1)
					buttonState.add("chckbxSaveloadModel");
				else if (buttonState.contains("chckbxSaveloadModel"))
					buttonState.remove("chckbxSaveloadModel");
					
				// TODO Auto-generated method stub
				if (chckbxSaveloadModel.isSelected()){
					btnBrowse2.setVisible(false);
					btnBrowse1.setVisible(true);
					lblModel.setVisible(false);
					lblSaveFile.setVisible(true);
					rdbtnLoadTrainedModel.setVisible(true);
					rdbtnLoadTrainedModel.setSelected(false);
					rdbtnSaveTrainedModel.setVisible(true);
					rdbtnSaveTrainedModel.setSelected(true);
				}else if (!chckbxSaveloadModel.isSelected()){
					btnBrowse2.setVisible(false);
					btnBrowse1.setVisible(false);
					lblModel.setVisible(false);
					lblSaveFile.setVisible(false);
					rdbtnLoadTrainedModel.setVisible(false);
					rdbtnLoadTrainedModel.setSelected(false);
					rdbtnSaveTrainedModel.setVisible(false);
					rdbtnSaveTrainedModel.setSelected(true);
					chckbxNewCheckBox.setEnabled(true);
				}
			}
		});
		
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (chckbxSaveloadModel.isSelected() && rdbtnLoadTrainedModel.isSelected() && validFile=="J48")
					mainids.impclsfier=(J48) ob;
				else if (chckbxSaveloadModel.isSelected() && rdbtnLoadTrainedModel.isSelected() && validFile=="Vote")
					mainids.impclsfier=(Vote) ob;
				else
					mainids.impclsfier=null;
				
				if (chckbxSaveloadModel.isSelected() && (mainids.impclsfier==null && saveModel==null)){
					JOptionPane.showMessageDialog(new JFrame(),"No file selected.\nPlease select a file or uncheck the \"Save/Load model\" option.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				setVisible(false);
				MainIDS.votingOpt.clear();
				mainids.setEnabled(true);
				mainids.requestFocus();
				MainIDS.votingOpt.add("-R");
				switch (comboBox.getSelectedIndex()){
				
				case 0:MainIDS.votingOpt.add("AVG");
				break;
				case 1:MainIDS.votingOpt.add("PROD");
				break;
				case 2:MainIDS.votingOpt.add("MAJ");
				break;
				case 3:MainIDS.votingOpt.add("MIN");
				break;
				case 4:MainIDS.votingOpt.add("MAX");
				break;
				}

				MainIDS.votingOpt.add("-S");
				MainIDS.votingOpt.add("1");
				flagentered=1;
				
				
				if (rdbtnLoadTrainedModel.isSelected() && mainids.impclsfier!=null && 
						mainids.impclsfier.getClass().getName()=="weka.classifiers.trees.J48"){
					mainids.rdbtnHanlet.setSelected(true);
					mainids.rdbtnNanlet.setSelected(false);
					mainids.rdbtnNanlet.setEnabled(false);
					mainids.rdbtnHanlet.setEnabled(true);
				}else if (rdbtnLoadTrainedModel.isSelected() && mainids.impclsfier!=null){
					mainids.rdbtnHanlet.setSelected(false);
					mainids.rdbtnHanlet.setEnabled(false);
					mainids.rdbtnNanlet.setSelected(true);
					mainids.rdbtnNanlet.setEnabled(true);
				}else{
					mainids.rdbtnHanlet.setEnabled(true);
					mainids.rdbtnNanlet.setEnabled(true);
				}
				
				//Clear state
				buttonState.clear();
			}
		});
		
		btnSave.setBounds(143, 291, 59, 23);
		contentPane.add(btnSave);

		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				setVisible(false);
				MainIDS.votingOpt.clear();
				mainids.setEnabled(true);
				mainids.requestFocus();
				
				MainIDS.votingOpt.add("-R");
				MainIDS.votingOpt.add("AVG");
				MainIDS.votingOpt.add("-S");
				MainIDS.votingOpt.add("1");
				
				//Reset to the saved state of user settings.
				if (flagentered==1){
					if (rdbtnFilterMethod.isSelected() && buttonState.contains("rdbtnFilterMethod")){
						rdbtnWrapperMethod.setSelected(true);
						
					}
					if (rdbtnWrapperMethod.isSelected() && buttonState.contains("rdbtnWrapperMethod")){
						rdbtnFilterMethod.setSelected(true);
					}
					
					if (rdbtnUseTestingSet.isSelected() && buttonState.contains("rdbtnUseTestingSet")){
						lblFolds.setVisible(true);
						txtFolds.setVisible(true);
						rdbtnUseCrossvalidation.setSelected(true);
						MainIDS.useTestSet=0;
					}else if (rdbtnUseCrossvalidation.isSelected() && buttonState.contains("rdbtnUseCrossvalidation")){
						rdbtnUseTestingSet.setSelected(true);
						lblFolds.setVisible(false);
						txtFolds.setVisible(false);
						MainIDS.useTestSet=1;
					}
					
					if (chckbxSaveloadModel.isSelected() && buttonState.contains("chckbxSaveloadModel")){
						rdbtnSaveTrainedModel.setVisible(false);
						btnBrowse2.setVisible(false);
						btnBrowse1.setVisible(false);
						rdbtnLoadTrainedModel.setVisible(false);
						chckbxSaveloadModel.setSelected(false);
						lblModel.setVisible(false);
						lblSaveFile.setVisible(false);
						chckbxNewCheckBox.setEnabled(true);
					}else if (!chckbxSaveloadModel.isSelected() && buttonState.contains("chckbxSaveloadModel")){
						rdbtnSaveTrainedModel.setVisible(true);
						rdbtnLoadTrainedModel.setVisible(true);
						chckbxSaveloadModel.setSelected(true);
						if (rdbtnSaveTrainedModel.isSelected()){
							btnBrowse1.setVisible(true);
							lblSaveFile.setVisible(true);
						}else{
							chckbxNewCheckBox.setEnabled(false);
							rdbtnFilterMethod.setVisible(false);
							rdbtnWrapperMethod.setVisible(false);
							btnBrowse2.setVisible(true);
							lblModel.setVisible(true);
						}
						
					}
					
					
					if (rdbtnSaveTrainedModel.isSelected() && buttonState.contains("rdbtnSaveTrainedModel")){
						rdbtnLoadTrainedModel.setSelected(true);
						btnBrowse2.setVisible(true);
						btnBrowse1.setVisible(false);
						lblModel.setVisible(true);
						lblSaveFile.setVisible(false);
						chckbxNewCheckBox.setEnabled(false);
						rdbtnFilterMethod.setVisible(false);
						rdbtnWrapperMethod.setVisible(false);
					}
					if (rdbtnLoadTrainedModel.isSelected() && buttonState.contains("rdbtnLoadTrainedModel")){
						rdbtnSaveTrainedModel.setSelected(true);
						btnBrowse2.setVisible(false);
						btnBrowse1.setVisible(true);
						lblSaveFile.setVisible(true);
						lblModel.setVisible(false);
						chckbxNewCheckBox.setEnabled(true);
					}
					
					
					if (chckbxNewCheckBox.isSelected() && buttonState.contains("chckbxNewCheckBox")){
						rdbtnFilterMethod.setVisible(false);
						rdbtnWrapperMethod.setVisible(false);
						chckbxNewCheckBox.setSelected(false);
					}else if (!chckbxNewCheckBox.isSelected() && buttonState.contains("chckbxNewCheckBox")){
						chckbxNewCheckBox.setSelected(true);
						rdbtnFilterMethod.setSelected(true);
						rdbtnFilterMethod.setVisible(true);
						rdbtnWrapperMethod.setVisible(true);
					}
					comboBox.setSelectedIndex(0);
					
				}
					buttonState.clear();
				
				//Reset everything if not saved.
				if (flagentered==0){
					main.impclsfier=null;
					saveModel=null;
					lblModel.setText("");
					lblSaveFile.setText("");
					chckbxSaveloadModel.setSelected(false);
					btnBrowse2.setVisible(false);
					btnBrowse1.setVisible(false);
					lblModel.setVisible(false);
					lblSaveFile.setVisible(false);
					rdbtnLoadTrainedModel.setVisible(false);
					rdbtnSaveTrainedModel.setVisible(false);
					rdbtnSaveTrainedModel.setSelected(true);
					chckbxNewCheckBox.setSelected(false);
					rdbtnWrapperMethod.setVisible(false);
					rdbtnFilterMethod.setSelected(true);
					rdbtnFilterMethod.setVisible(false);
					rdbtnUseTestingSet.setSelected(true);
					lblFolds.setVisible(false);
					txtFolds.setVisible(false);
					txtFolds.setText("10");
					comboBox.setSelectedIndex(0);
					chckbxNewCheckBox.setEnabled(true);
					MainIDS.useTestSet=1;
				}
				
			}
		});
		btnCancel.setBounds(212, 291, 66, 23);
		contentPane.add(btnCancel);
		
		
		JButton btnReset = new JButton("Reset All");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Reset the use testset/cross val. boxes
				rdbtnUseTestingSet.setSelected(true);
				rdbtnUseCrossvalidation.setSelected(false);
				lblFolds.setVisible(false);
				txtFolds.setVisible(false);
				//Reset var for importing models
				main.impclsfier=null;
				saveModel=null;
				lblModel.setText("");
				lblSaveFile.setText("");
				comboBox.setSelectedIndex(0);
				//Reset filter boxes
				chckbxNewCheckBox.setSelected(false);
				rdbtnFilterMethod.setVisible(false);
				rdbtnWrapperMethod.setVisible(false);
				btnBrowse2.setVisible(false);
				btnBrowse1.setVisible(false);
				//Reset save/load model checkboxes
				rdbtnLoadTrainedModel.setSelected(false);
				rdbtnLoadTrainedModel.setVisible(false);
				rdbtnSaveTrainedModel.setSelected(true);
				rdbtnSaveTrainedModel.setVisible(false);
				chckbxSaveloadModel.setSelected(false);
				mainids.rdbtnHanlet.setSelected(true);
				mainids.rdbtnNanlet.setSelected(false);
				mainids.rdbtnNanlet.setEnabled(true);
				mainids.rdbtnHanlet.setEnabled(true);
				}
		});
		btnReset.setBounds(353, 291, 75, 23);
		contentPane.add(btnReset);
		
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
				MainIDS.votingOpt.clear();
				mainids.setEnabled(true);
				mainids.requestFocus();
				
				MainIDS.votingOpt.add("-R");
				MainIDS.votingOpt.add("AVG");
				MainIDS.votingOpt.add("-S");
				MainIDS.votingOpt.add("1");
			}
			
		});

		
	}
}
