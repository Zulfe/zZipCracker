package zZipCracker2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;



public class ZipCracker {
	
	//cracking related
	private static String fileName;
	private static JFileChooser userFile = Main.userFile;
	//private static FileNameExtensionFilter filterZipTxt = new FileNameExtensionFilter("Zip Files", "zip", "txt"); //uncomment and use if you have issues with filters in main

	//GUI shorthands
	private static String uT = GUI.userText;
	private static JTextArea dis = GUI.display;
	private static JFrame f = GUI.frame;

	//control flow related
	private static boolean userContinue = true;
	private static boolean userCrack = true;
	
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
				f.setVisible(true);
				

				
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
							
							//GUI display printing, row number control, and debugging
							String windowText = "Testing password no." + passwordCounter + ", which is " + passwordArray.get(0);
							dis.append(windowText + "\n"); //text for user
							dis.setCaretPosition(dis.getDocument().getLength()); //make printed text move with window
							System.out.println("LINE COUNT: " + dis.getLineCount());
							
							//if there are more than 17 lines printed in the field, kill the top line.
							//this helps with memory consumption at the cost of limiting the user's
							//view of used passwords. this isn't much of an issue since the user
							//will receive the found password at the end.
							if(dis.getLineCount() > 17){
								dis.replaceRange("", 0, windowText.length() + 7);
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
	
	
	//in-loop commands
	public static void inputHandlerInner(){
		//user input handler
		if(uT.equals("STOP")){
			userCrack = false;
			dis.append("PROGRAM HAS BEEN PAUSED");
			GUI.userInput.setText("");
		}
		else if(GUI.userText.equals("KILL")){
			userCrack = false;
			userContinue = false;
		}
	}
	
	//out-loop commands
	public static void inputHandlerOuter(){
		//user input handler
		if(uT.equals("CONTINUE") && userCrack == false){
			userCrack = true;
			dis.append("PROGRAM HAS BEEN CONTINUED");
			GUI.userInput.setText("");
		}
		else if (GUI.userText.equals("KILL") && userCrack == false)
			userContinue = false;
	}
	
	
	
	
}
