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


package zZipCracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane; 

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import javax.swing.filechooser.FileNameExtensionFilter;

public class zZipCracker {
	
	//declarations for the global file name (for the .zip and .txt dictionary), the reused file chooser,
	//and the filter for the file chooser.
	private static String fileName;
	private static JFileChooser userFile = new JFileChooser();
	private static FileNameExtensionFilter filterZipTxt = new FileNameExtensionFilter("Zip Files", "zip", "txt");
	
	public static void main(String[] args) throws ZipException, IOException{
		userFile.setFileFilter(filterZipTxt); //this must be declared in the main, issues with the file
											  //chooser not opening have occurred when run elsewhere
		
		//Program directory window.
	    String[] buttons = {"Cancel", "zDictionaryForm", "zZipCracker"};
	    int rc = JOptionPane.showOptionDialog(null,
	    									  "Which program would you like to use?",
	    									  "Program Directory",
	    									  JOptionPane.WARNING_MESSAGE,
	    									  0, null, buttons, buttons[0]);
	    
	    //System.out.println(rc); Debugging for control flow on buttons.
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
				BufferedReader br = new BufferedReader(new FileReader(fileName));
				while(true) { //while elements are still in the array list
					String line;
					if ((line = br.readLine()) != null) {
						if(line.startsWith(" ")){
							passwordArray.add("nullObject");
							System.out.println("********nullObject********");
						} else{
							passwordArray.add(line);
						}
					}
					//System.out.println("password I'm passing is: " + (String) passwordArray.get(0));
						zipper.setPassword((String) passwordArray.get(0)); //set the password to element position [passwordCounter]
						System.out.println("Testing password no." + passwordCounter + ", which is " + passwordArray.get(0));
						passwordCounter = passwordCounter + 1;
						String currentPassword = (String) passwordArray.get(0);
						
					if(currentPassword.length() > 0){
						try {
							zipper.extractAll(dest);
						 	br.close();
						 	JOptionPane.showMessageDialog(null, "The zip has been cracked. The password is " + passwordArray.get(0));
						 	break;
						} catch(ZipException ze) {
							if(passwordArray.size() > 1)
								System.out.println("Memory check: ArrayList consists of " + passwordArray.size() + " items.");
							else
								System.out.println("Memory check: ArrayList consists of " + passwordArray.size() + " item.");
							passwordArray.remove(0);
							continue;
						}
					}
				}
		} else {
				zipper.extractAll(dest);
				JOptionPane.showMessageDialog(null, "The selected zip was not password protected. It was extracted anyways.");
		}
		
		System.exit(0); //kills the program
	}
	
	//uses a GUI file chooser to make it easy for the user to choose a specific directory
	public static String filePick(){
		int returnVal = userFile.showOpenDialog(null); //open file chooser
		
		if (returnVal == JFileChooser.APPROVE_OPTION)
			fileName = userFile.getSelectedFile().toString();
		else
			fileName = "The file open operation failed.";
		
		return fileName;
	}
	
}
