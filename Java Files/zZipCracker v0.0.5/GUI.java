package zZipCracker2;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class GUI {

	public static JFrame frame = new JFrame();
	public static String userText = new String();
	public static JTextArea display = new JTextArea(16, 27);
	public static JTextField userInput = new JTextField(10);
	
	private static JPanel panel = new JPanel();
	private static JScrollPane scroll = new JScrollPane(display);
	
	public static void buildGUI(){
		//text area
		display.setEditable(false);
		
		//text field
		userInput.setEditable(true);
		userInput.setColumns(1);
		userInput.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				userText = userInput.getText();
			}
		});
		
		//command label & panel
		JPanel commandPanel = new JPanel();
		
		JLabel commandLabel = new JLabel();
		JLabel commandLabel2 = new JLabel();
		JLabel commandLabel3 = new JLabel();
		JLabel commandLabel4 = new JLabel();

		commandLabel.setText("USER COMMANDS:");
		commandLabel2.setText("STOP - PAUSE PROGRAM");
		commandLabel3.setText("CONTINUE - RESUME PROGRAM");
		commandLabel4.setText("KILL - END PROGRAM");
		
		Box commandBox = Box.createVerticalBox();
		
		commandBox.add(commandLabel);
		commandBox.add(Box.createVerticalStrut(4));
		commandBox.add(commandLabel2);
		commandBox.add(Box.createVerticalStrut(4));
		commandBox.add(commandLabel3);
		commandBox.add(Box.createVerticalStrut(4));
		commandBox.add(commandLabel4);
		commandBox.add(Box.createVerticalStrut(15));
		commandBox.add(userInput);
		
		commandPanel.add(commandBox);
		
		//scroll panel
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setSize(175, 250);
		
		//jpanel
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.add(scroll);
		panel.add(commandPanel);

		//panel.setBorder(new TitledBorder(new EtchedBorder(), "Passwords"));
		
		//jframe
		
		frame.setTitle("zZipCracker Password Attack");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
}
