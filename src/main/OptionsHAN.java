package main;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

/*
 * This class is a JFrame.It implements the HAN-let options where
 * user can select parameters for J48 classifier.All the selections
 * are saved in HANarrayopt.
 */
public class OptionsHAN extends JFrame {

	private JPanel contentPane;
	private JTextField txtConfthres;
	private JTextField textNumfolds;
	private JTextField txtMinInst;
	private MainIDS mainids;
	private JTextField txtSeed;
	public ArrayList<String> HANarrayopt=new ArrayList<String>();	

	/**
	 * Create the frame.
	 */
	public OptionsHAN(MainIDS main) {
		mainids=main;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setTitle("Options");
		setBounds(100, 100, 475, 310);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblHanlet = new JLabel("HAN-let (J48) options");
		lblHanlet.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblHanlet.setBounds(128, 11, 142, 19);
		contentPane.add(lblHanlet);

		JCheckBox chckbxUnprunedTree = new JCheckBox("Unpruned tree");
		chckbxUnprunedTree.setBounds(27, 32, 121, 19);
		contentPane.add(chckbxUnprunedTree);

		JCheckBox chckbxDontCollapse = new JCheckBox("Do not collapse tree");
		chckbxDontCollapse.setBounds(27, 54, 135, 23);
		contentPane.add(chckbxDontCollapse);

		txtConfthres = new JTextField();
		txtConfthres.setText("0.25");
		txtConfthres.setBounds(399, 89, 46, 19);
		contentPane.add(txtConfthres);
		txtConfthres.setColumns(10);

		JLabel lblConfidenceThreshold = new JLabel("<html>Confidence threshold<br>for pruning:</html>");
		lblConfidenceThreshold.setBounds(280, 76, 109, 32);
		contentPane.add(lblConfidenceThreshold);

		JLabel lblNumfolds = new JLabel("<html>Number of folds<br>(for error pruning):</html>");
		lblNumfolds.setBounds(283, 181, 98, 36);
		lblNumfolds.setVisible(false);
		contentPane.add(lblNumfolds);

		textNumfolds = new JTextField();
		textNumfolds.setText("3");
		textNumfolds.setBounds(399, 192, 46, 19);
		textNumfolds.setVisible(false);
		contentPane.add(textNumfolds);
		textNumfolds.setColumns(10);

		JCheckBox chckbxUseReducedError = new JCheckBox("Use reduced error pruning");
		
		chckbxUseReducedError.setBounds(281, 165, 151, 19);
		contentPane.add(chckbxUseReducedError);


		JCheckBox chckbxBinary = new JCheckBox("Binary splits only");
		chckbxBinary.setBounds(27, 80, 109, 19);
		contentPane.add(chckbxBinary);

		JCheckBox chckbxSubtreeRaising = new JCheckBox("No subtree raising");
		chckbxSubtreeRaising.setBounds(27, 108, 121, 19);
		contentPane.add(chckbxSubtreeRaising);

		chckbxUnprunedTree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (chckbxUnprunedTree.isSelected())
					chckbxSubtreeRaising.setEnabled(false);
				else
					chckbxSubtreeRaising.setEnabled(true);
			}
		});

		JCheckBox chckbxLaplaceSmoothing = new JCheckBox("<html>Laplace smoothing for <br>predicted probabilities</html>");
		chckbxLaplaceSmoothing.setBounds(27, 130, 135, 32);
		contentPane.add(chckbxLaplaceSmoothing);

		JCheckBox chckbxDoNotUse = new JCheckBox("<html>Do not use MDL correction for <br>info gain on numeric attributes</html>");
		chckbxDoNotUse.setBounds(280, 32, 177, 37);
		contentPane.add(chckbxDoNotUse);

		JCheckBox chckbxDoNotMake = new JCheckBox("<html>Do not make split point<br>actual value</html>");
		chckbxDoNotMake.setBounds(27, 170, 135, 22);
		contentPane.add(chckbxDoNotMake);

		JLabel lblSetMinimumNumber = new JLabel("<html>Set minimum number of<br>instances per leaf:</html>");
		lblSetMinimumNumber.setBounds(280, 119, 121, 37);
		contentPane.add(lblSetMinimumNumber);

		JLabel lblSeedForRandom = new JLabel("<html>Seed for random<br>data shuffling:</html>");
		lblSeedForRandom.setBounds(293, 217, 80, 37);
		lblSeedForRandom.setVisible(false);
		contentPane.add(lblSeedForRandom);

		txtSeed = new JTextField();
		txtSeed.setVisible(false);
		txtSeed.setText("1");
		txtSeed.setBounds(399, 225, 46, 19);
		contentPane.add(txtSeed);
		txtSeed.setColumns(10);


		txtMinInst = new JTextField();
		txtMinInst.setText("2");
		txtMinInst.setBounds(398, 132, 46, 19);
		contentPane.add(txtMinInst);
		txtMinInst.setColumns(10);

		JButton btnBack = new JButton("Save");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainids.setEnabled(true);
				mainids.requestFocus();
				setVisible(false);
				HANarrayopt.clear();
				if (HANarrayopt!=null){
					HANarrayopt.add("-M");HANarrayopt.add(txtMinInst.getText());					
					if (chckbxBinary.isSelected())
						HANarrayopt.add("-B");
					if (chckbxUnprunedTree.isSelected()){
						HANarrayopt.add("-U");
					}else if (txtConfthres.isEnabled()){
						HANarrayopt.add("-C");
						HANarrayopt.add(txtConfthres.getText());
					}
					if (chckbxDontCollapse.isSelected())
						HANarrayopt.add("-O");
					if (!chckbxUnprunedTree.isSelected() && chckbxUseReducedError.isSelected()){
						HANarrayopt.add("-R");
						HANarrayopt.add("-N");HANarrayopt.add(textNumfolds.getText());}
					if (chckbxSubtreeRaising.isEnabled() && chckbxSubtreeRaising.isSelected())
						HANarrayopt.add("-S");
					if (chckbxLaplaceSmoothing.isSelected())
						HANarrayopt.add("-A");
					if (!txtSeed.getText().equals("1")){
						HANarrayopt.add("-Q");HANarrayopt.add(txtSeed.getText());}
					if (chckbxDoNotMake.isSelected())
						HANarrayopt.add("-doNotMakeSplitPointActualValue");

				}

			}
		});
		btnBack.setBounds(143, 238, 65, 23);
		contentPane.add(btnBack);
		
		chckbxBinary.setSelected(false);
		chckbxUnprunedTree.setSelected(false);
	  	chckbxDontCollapse.setSelected(false);
	  	chckbxUseReducedError.setSelected(false);
	  	chckbxSubtreeRaising.setSelected(false);
	  	chckbxLaplaceSmoothing.setSelected(false);
	  	chckbxDoNotMake.setSelected(false);
	  	chckbxSubtreeRaising.setEnabled(true);
	  	txtConfthres.setEnabled(true);
		chckbxUnprunedTree.setEnabled(true);
		
		JCheckBox chckbxVisualizeTree = new JCheckBox("Visualize tree");
		chckbxVisualizeTree.setBounds(27, 204, 97, 23);
		contentPane.add(chckbxVisualizeTree);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxBinary.setSelected(false);
				chckbxUnprunedTree.setSelected(false);
			  	chckbxDontCollapse.setSelected(false);
			  	chckbxUseReducedError.setSelected(false);
			  	chckbxSubtreeRaising.setSelected(false);
			  	chckbxLaplaceSmoothing.setSelected(false);
			  	chckbxDoNotMake.setSelected(false);
			  	chckbxSubtreeRaising.setEnabled(true);
			  	chckbxVisualizeTree.setSelected(false);
			  	chckbxDoNotUse.setSelected(false);
			  	lblNumfolds.setVisible(false);
			  	lblSeedForRandom.setVisible(false);
			  	txtSeed.setVisible(false);
			  	textNumfolds.setVisible(false);
			  	txtConfthres.setEnabled(true);
				chckbxUnprunedTree.setEnabled(true);
				HANarrayopt.clear();
			}
		});
		btnReset.setBounds(218, 238, 61, 23);
		contentPane.add(btnReset);
		chckbxVisualizeTree.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (chckbxVisualizeTree.isSelected())
					MethodsIDS.visual=1;
				else
					MethodsIDS.visual=0;
			}
		});
		
		chckbxUseReducedError.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxUseReducedError.isSelected()){
					textNumfolds.setVisible(true);
					lblNumfolds.setVisible(true);
					lblSeedForRandom.setVisible(true);
					txtSeed.setVisible(true);
					txtConfthres.setEnabled(false);
					chckbxUnprunedTree.setEnabled(false);
				}else{
					textNumfolds.setVisible(false);
					lblNumfolds.setVisible(false);
					lblSeedForRandom.setVisible(false);
					txtSeed.setVisible(false);
					txtConfthres.setEnabled(true);
					chckbxUnprunedTree.setEnabled(true);
				}

			}
		});
		
		this.addWindowListener(new WindowAdapter() {
		
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		setVisible(false);
		mainids.setEnabled(true);
		mainids.requestFocus();
		HANarrayopt.clear();	
	}
	
});



	}
}
