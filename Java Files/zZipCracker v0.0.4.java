/*
 * @author Damon Shaw, zulfe@live.com
 * @copyright 2014
 * @license GPLv3
 */

/*
 * This program utilizes the Zip4j library by Srikanth Reddy Lingala. I by no means take credibility
 * for the creation, development, maintaining, or documentation for this software and are hereby
 * declaring my use solely for the purpose of education and separate program development.
 * 
 * Author - Srikanth Reddy Lingala
 * Site - http://www.lingala.net/zip4j/
 * Resource Used - Zip4j
 * Resource License - Apache 2.0
 * Date Accessed - 11/23/14 to 11/24/14
 */


package zipCracker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane; 
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.FlowLayout;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import javax.swing.filechooser.FileNameExtensionFilter;


public class zZipCracker {
	
	//declarations for the global file name (for the .zip and .txt dictionary), the reused file chooser,
	//and the filter for the file chooser.
	
	//cracking related
	private static String fileName;
	private static JFileChooser userFile = new JFileChooser();
	private static FileNameExtensionFilter filterZipTxt = new FileNameExtensionFilter("Zip Files", "zip", "txt");
	
	//GUI related
	private static JPanel panel = new JPanel();
	private static JTextArea display = new JTextArea(16, 27);
	private static JTextField userInput = new JTextField(20);
	private static JScrollPane scroll = new JScrollPane(display);
	private static JFrame frame = new JFrame();
	private static String userText = new String();
	
	//control flow related
	private static boolean userContinue = true;
	private static boolean userCrack = true;
	
	public static void main(String[] args) throws ZipException, IOException{
		userFile.setFileFilter(filterZipTxt); //this must be declared in the main, issues with the file
											  //chooser not opening have occurred when run elsewhere
		initFrameComponents(); //build the GUI
		
		
		
		
		//Program directory window.
	    String[] buttons = {"Cancel", "zDictionaryForm", "zZipCracker"};
	    int rc = JOptionPane.showOptionDialog(null,
	    									  "Which program would you like to use?",
	    									  "Program Directory",
	    									  JOptionPane.WARNING_MESSAGE,
	    									  0, null, buttons, buttons[0]);
	    
	    System.out.println(rc); //Debugging for control flow on buttons.
	    //Control flow for window
	    if(rc == 2)
	    	zZipCracker(); //start cracking
	    else if(rc == 1)
	    	System.exit(0); //build dictionary. not added as of [11/24/14]
	    else
	    	System.exit(0); //exit
	    JOptionPane.showMessageDialog(null, "The zip could not be cracked, try a larger dictionary " +
	    									"or report this issue to the software developer."); //debugging for program end location
	}
	
	public static void zZipCracker() throws ZipException, IOException{
		fileName = filePick(); //get the file's path using the file chooser GUI
		System.out.println(fileName); //used for debugging
		
		ZipFile zipper = new ZipFile(fileName); //instantiate the zip as a zip4j ZipFile object
		
		String dest = JOptionPane.showInputDialog("Please input a destination for the unzipped file." + "\n" +
												  "Triple check the path, as this program WILL overwrite directories.");
		
		@SuppressWarnings("rawtypes")
		ArrayList passwordArray = new ArrayList(); //create a list to hold the values from a dictionary
		int passwordCounter = 0; //used for positioning in the case of an encrypted zip

		if(zipper.isEncrypted()){ //should return true for all types of encryption
				JOptionPane.showMessageDialog(null, "You will now select your .txt dictionary.");
				fileName = filePick();
				
				//reader to pull items from the dictionary
				BufferedReader br = new BufferedReader(new FileReader(fileName));
				frame.setVisible(true);
				

				
				//cracking process shell for pausing/continuing
				while(userContinue == true) {
					
					//check for user commands for program control flow
					inputHandlerOuter();
						
					//cracking process
					while(userCrack == true) { //while the user has not run a command to stop the loop
						
						//null object identifier
	                    String line;
	                    if ((line = br.readLine()) != null) {
		                        if(line.startsWith(" ") || line.length() == 0){
		                            passwordArray.add("nullObject");
		                            System.out.println("********nullObject********");
		                        } else{
		                            passwordArray.add(line);
		                        }
		                    
		                    //debugging and setting the zip pass
		                    System.out.println(passwordArray.get(0));
							zipper.setPassword((String) passwordArray.get(0)); //set the password to element position [passwordCounter]
							
							//current password and line debugging
							System.out.println("Testing password no." + passwordCounter + ", which is " + passwordArray.get(0));
							
							//GUI display printing and row number control
							String windowText = "Testing password no." + passwordCounter + ", which is " + passwordArray.get(0);
							display.append(windowText + "\n"); //text for user
							display.setCaretPosition(display.getDocument().getLength()); //make printed text move with window
							System.out.println("LINE COUNT: " + display.getLineCount());
							if(display.getLineCount() > 17){
								display.replaceRange("", 0, windowText.length() + 7);
							}
							
							//password counter and password to variable storage
							passwordCounter = passwordCounter + 1;
							String currentPassword = (String) passwordArray.get(0);
							
							//if the password is an actual string that can be used on the zip
							if(currentPassword.length() > 0){
								
								//try to open the zip file. if it works, close the reader, alert the user of the password
								//and end the loop
								try {
									zipper.extractAll(dest);
									br.close();
									JOptionPane.showMessageDialog(null, "The zip has been cracked. The password is " + passwordArray.get(0));
									break;
								
								 //if a ZipException (unique to the Zip4j library) occurs, suppress it, remove the
								 //password that doesn't work, check for user commands, and move to next password
								} catch(ZipException ze) {
									
									//debugging
									System.out.println(passwordArray.size());
									
									//memory management - keep the list at 1 element
									passwordArray.remove(0);
									
									//checks for user commands to stop/kill program
				                    //MUST BE LOCATED AFTER THE LOOP OR LOOP DATA WILL PRINT AFTER
				                    //CONTROL FLOW PRINTS
									inputHandlerInner();
									
									//zip has not been cracked, go to next line
									continue;
								  }
							}
						  //if the zip doesn't have a password, just open it. tell user that the zip is not locked
	                    } else {
	                    	zipper.extractAll(dest);
	                    	JOptionPane.showMessageDialog(null, "The selected zip was not password protected. It was extracted anyways.");
	                    	
	                    }	

					}
			
			}	
			System.exit(0); //kills the program
		}
	}
	
	//uses a GUI file chooser to make it easy for the user to choose a specific directory
	public static String filePick(){
		int returnVal = userFile.showOpenDialog(null); //open file chooser
		
		if (returnVal == JFileChooser.APPROVE_OPTION)
			fileName = userFile.getSelectedFile().toString();
		else{
			fileName = "The file open operation failed.";
			System.exit(0);
		}
		
		return fileName;
	}
	
	//GUI setup
	public static void initFrameComponents(){
		//text area
		display.setEditable(false);
		
		//text field
		userInput.setEditable(true);
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
		commandLabel3.setText("CONTINUE - RESUMED STOPPED PROGRAM");
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
	
	//in-loop commands
	public static void inputHandlerInner(){
		//user input handler
		if(userText.equals("STOP")){
			userCrack = false;
			display.append("PROGRAM HAS BEEN PAUSED");
			userInput.setText("");
		}
		else if(userText.equals("KILL")){
			userCrack = false;
			userContinue = false;
		}
	}
	
	//out-loop commands
	public static void inputHandlerOuter(){
		//user input handler
		if(userText.equals("CONTINUE") && userCrack == false){
			userCrack = true;
			display.append("PROGRAM HAS BEEN CONTINUED");
			userInput.setText("");
		}
		else if (userText.equals("KILL") && userCrack == false)
			userContinue = false;
	}
	
}
