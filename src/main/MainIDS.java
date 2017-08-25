package main;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.Vote;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JEditorPane;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;

/*
* Author: Philokypros Ioulianou
* This is the main class of SNIDS.It shows the main screen
* of the software.User can run HAN-let or NAN-let by
* importing a training and a testing dataset.
*/
public class MainIDS extends JFrame {

	private JPanel contentPane;
	MethodsIDS meth;
	Instances trainset,testset;
	public ArrayList<String> mainNANVoteOpt=new ArrayList<String>(),
			mainHANopt=new ArrayList<String>();
	static ArrayList<String> votingOpt=new ArrayList<String>();
	int flag=0,flag2=0,testloaded=0,trainloaded=0,flagGen=0,chkboxSel=1;
	OptionsHAN optHAN;
	OptionsNAN optNAN;
	OptionsGen genOpt;
	static int useTestSet=1,expFile=0,expModel=0;
	String expFilename=null;
	Classifier impclsfier=null;
	JRadioButton rdbtnHanlet,rdbtnNanlet;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainIDS frame = new MainIDS();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*
	 * Import NAN-let model
	 */
	public Vote readNANModel(String file){
		Vote vote=null;
			try {
				vote = (Vote) weka.core.SerializationHelper.read(file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.err.println("Not a valid model file!");
				e.printStackTrace();
			}
		 return vote;
	}
	
	/*
	 * Import J48 model into SNIDS
	 */
	public J48 readHANModel(String file){
		 J48 jcls=null;
		try {
			 jcls = (J48) weka.core.SerializationHelper.read(file);
			
		} catch (Exception e) {
			System.err.println("Not a valid model file!");
			JOptionPane.showMessageDialog(new JFrame(),  "Not a valid model file.Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		 return jcls;
	}


	/**
	 * Create the frame.
	 */
	public MainIDS() {

		MainIDS main=this;
		meth=new MethodsIDS();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setTitle("SNIDS software");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 408);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblSnids = new JLabel("Smart Network-based IDS (SNIDS)");
		lblSnids.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSnids.setBounds(197, 30, 220, 23);
		contentPane.add(lblSnids);

		JPanel panel = new JPanel();
		panel.setBounds(24, 59, 162, 202);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblOptions = new JLabel("Choose an option:");
		lblOptions.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblOptions.setBounds(10, 11, 109, 25);
		panel.add(lblOptions);

		rdbtnHanlet = new JRadioButton("HAN-let");
		rdbtnHanlet.setSelected(true);
		rdbtnHanlet.setBounds(10, 33, 69, 23);
		panel.add(rdbtnHanlet);

		rdbtnNanlet = new JRadioButton("NAN-let");
		rdbtnNanlet.setBounds(10, 59, 69, 25);
		panel.add(rdbtnNanlet);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnHanlet);
		group.add(rdbtnNanlet);

		JLabel lblname = new JLabel("");
		lblname.setBounds(85, 153, 67, 25);
		panel.add(lblname);

		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setVisible(false);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Choose the file to save predictions. The file will be a .csv file.
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(null);
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Text and csv files ","txt","csv");
				chooser.setFileFilter(filter);
				if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
					expFilename=chooser.getSelectedFile()+".csv";
					lblname.setText(chooser.getSelectedFile().getName()+".csv");	
				}else{
					expFilename=null;
					lblname.setText("Not selected");
				}
			}
		});
		btnBrowse.setBounds(6, 153, 69, 23);
		panel.add(btnBrowse);

		JCheckBox chckbxExportPredictionsTo = new JCheckBox("Export predictions to file");
		chckbxExportPredictionsTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Show or hide the browse button accordingly.
				if (chckbxExportPredictionsTo.isSelected()){
					btnBrowse.setVisible(true);
					lblname.setVisible(true);
					expFile=1;
				}else{
					expFile=0;
					btnBrowse.setVisible(false);
					lblname.setVisible(false);
				}
			}
		});
		chckbxExportPredictionsTo.setBounds(6, 121, 146, 23);
		panel.add(chckbxExportPredictionsTo);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 634, 23);
		contentPane.add(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(197, 59, 426, 299);
		contentPane.add(panel_1);

		JLabel lblWelcomeToSnids = new JLabel("<html><br><center>Welcome to SNIDS!</center><br>Please select the proper options and press the \"Start\" button.<br>For further guidance go to Help->Instructions.</html>");
		lblWelcomeToSnids.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(lblWelcomeToSnids);

		JEditorPane txtDisplay = new JEditorPane();

		txtDisplay.setVisible(true);
		txtDisplay.setEditable(false);
		txtDisplay.setMinimumSize(new Dimension(395, 277));
		txtDisplay.setPreferredSize(new Dimension(410, 287));

		JScrollPane txtscrollPane = new JScrollPane(txtDisplay);
		txtscrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		txtscrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		

		JMenuItem mntmImportData = new JMenuItem("1. Import train data...");
		mntmImportData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Import training data from file. File can be any type.
				trainloaded=1;
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"ARFF, XARFF, CSV, C4.5 DATA and NAMES files ", "arff", "xarff","csv","data","names");
				fileChooser.setFileFilter(filter);
				if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					panel_1.removeAll();
					panel_1.setVisible(true);
					panel_1.add(txtscrollPane);
					panel_1.revalidate();
					panel_1.repaint();
					File file = fileChooser.getSelectedFile();
					System.out.println(file.getPath());
					trainset=meth.getData(file.getPath(),0);
					txtDisplay.setText(trainset.toString() +"\nReading data...Done!");
				}

			}
		});

		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		mnFile.add(mntmImportData);

		JMenuItem mntmImportTest = new JMenuItem("2. Import test data...");
		mntmImportTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Import testing dataset
				testloaded=1;
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"ARFF, XARFF, CSV, C4.5 DATA and NAMES files ", "arff", "xarff","csv","data","names");
				fileChooser.setFileFilter(filter);
				if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					
					File file = fileChooser.getSelectedFile();
					testset=meth.getData(file.getPath(),1);
					txtDisplay.setText(testset.toString() +"\nReading test data...Done!");
				}
			}
		});
		mnFile.add(mntmImportTest);

		JMenu mnNewMenu = new JMenu("Help");
		menuBar.add(mnNewMenu);

		JMenuItem mntmInstructions = new JMenuItem("Instructions");
		mntmInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(new JFrame(),  "Go to File->Import data to import a dataset "
						+ "and then \nchoose the HAN-let or NAN-let option to start detection.", "Instructions", JOptionPane.INFORMATION_MESSAGE);

			}
		});
		mnNewMenu.add(mntmInstructions);

		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		JMenuItem mntmOptions = new JMenuItem("HAN-let options");
		mnOptions.add(mntmOptions);

		JMenuItem mntmOptionsForNanlet = new JMenuItem("NAN-let options");
		mnOptions.add(mntmOptionsForNanlet);

		JMenuItem mntmGeneralOptions = new JMenuItem("General options");
		mnOptions.add(mntmGeneralOptions);
		mntmGeneralOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Show or hide the general options frame.
				setEnabled(false);
				if (flagGen==1)
					genOpt.setVisible(true);
				else{
					genOpt=new OptionsGen(main);
					genOpt.setVisible(true);
				}
				flagGen=1;

			}
		});
		mntmOptionsForNanlet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setEnabled(false);
				if (flag2==1)
					optNAN.setVisible(true);
				else{
					optNAN=new OptionsNAN(main);
					optNAN.setVisible(true);
				}
				flag2=1;

			}
		});

		mntmOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabled(false);
				if (flag==1)
					optHAN.setVisible(true);
				else{
					optHAN=new OptionsHAN(main);
					optHAN.setVisible(true);
				}
				
				flag=1;
				meth.setOptionsHAN(optHAN.HANarrayopt);
				mainHANopt=optHAN.HANarrayopt;
			}
		});



		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});
		btnExit.setBounds(54, 319, 89, 23);
		contentPane.add(btnExit);

		JButton btnBegin = new JButton("Start");
		btnBegin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Show message if train data not loaded.
				if (trainloaded!=1){
					JOptionPane.showMessageDialog(new JFrame(),  "Please load the training dataset!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				//Show message if test data not loaded (and testing with dataset is selected).
				if (useTestSet==1 && testloaded!=1){
					JOptionPane.showMessageDialog(new JFrame(),  "Please load the testing dataset!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (expFilename==null && expFile==1){
					JOptionPane.showMessageDialog(new JFrame(),  "Please load the file to export predictions!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
	
				
				//Run J48 HAN-let
				if (rdbtnHanlet.isSelected()){

					if (flag!=1){
						txtDisplay.setText("Running using default settings...");
						mainHANopt.clear();
						meth.setOptionsHAN(mainHANopt);
					}else
						meth.setOptionsHAN(mainHANopt);
					txtDisplay.setText("Running...");

					Runnable runnable = () -> {
						Evaluation ev=meth.trainHANEval(expFilename,(J48)impclsfier);
						try {
							panel_1.removeAll();
							panel_1.setVisible(true);
							panel_1.add(txtscrollPane);
							panel_1.revalidate();
							panel_1.repaint();
							if (useTestSet==1)
								txtDisplay.setText(ev.toSummaryString("\nResults of test dataset\n======\n", false)+"\n"
										+ev.toClassDetailsString()+"\n"+ev.toMatrixString());
							else
								txtDisplay.setText(ev.toSummaryString("\nResults of cross-validation\n======\n",false)+"\n"
										+ev.toClassDetailsString()+"\n"+ev.toMatrixString());
							
						} catch (Exception e) {
							System.err.print("Error in printing details.");
							e.printStackTrace();
						}
					};
					Executor ex=Executors.newCachedThreadPool();
					ex.execute(runnable);

				}else if(rdbtnNanlet.isSelected()){
					//Run NAN-let with RF and SVM.
					if (flag2!=1){
						txtDisplay.setText("Running using default settings...");
						
						//Maybe entered general options.Not entered NAN options.
						meth.setOptionsNAN(mainNANVoteOpt,null,null);
					}else{
						
						txtDisplay.setText("Running...");
						//Check if voting options are empty from general opt. 
						if (votingOpt.isEmpty()){
							mainNANVoteOpt.add("-R");
							mainNANVoteOpt.add("AVG");
							mainNANVoteOpt.add("-S");
							mainNANVoteOpt.add("1");
						}else{
							mainNANVoteOpt.add(votingOpt.get(0).toString());
							mainNANVoteOpt.add(votingOpt.get(1).toString());
							mainNANVoteOpt.add(votingOpt.get(2).toString());
							mainNANVoteOpt.add(votingOpt.get(3).toString());
						}

						meth.setOptionsNAN(mainNANVoteOpt,optNAN.NANarroptSVM,optNAN.NANarroptRF);
					}
					Runnable runnable = () -> {
						Evaluation ev=meth.trainNANEval(expFilename,(Vote) impclsfier);
						panel_1.removeAll();
						panel_1.setVisible(true);
						panel_1.add(txtscrollPane);
						panel_1.revalidate();
						panel_1.repaint();
						if (useTestSet==1)
							try {
								txtDisplay.setText(ev.toSummaryString("\nResults of test dataset\n======\n", false)+"\n"
										+ev.toClassDetailsString()+"\n"+ev.toMatrixString());
							} catch (Exception e) {
								System.err.print("Error in printing details.");
								e.printStackTrace();
							}
						else
							try {
								txtDisplay.setText(ev.toSummaryString("\nResults of cross-validation\n======\n",false)+"\n"
										+ev.toClassDetailsString()+"\n"+ev.toMatrixString());
							} catch (Exception e) {
								System.err.print("Error in printing details.");
								e.printStackTrace();
							}
					};
					Executor ex=Executors.newCachedThreadPool();
					ex.execute(runnable);

				}
			}
		});
		btnBegin.setBounds(54, 285, 89, 23);
		contentPane.add(btnBegin);

	}
}
