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


package zZipCracker2;


import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane; 

import net.lingala.zip4j.exception.ZipException;

import javax.swing.filechooser.FileNameExtensionFilter;


public class Main {
	
	//declarations for the global file name (for the .zip and .txt dictionary), the reused file chooser,
	//and the filter for the file chooser.
	
	//cracking related
	public static JFileChooser userFile = new JFileChooser();
	private static FileNameExtensionFilter filterZipTxt = new FileNameExtensionFilter("Zip Files", "zip", "txt");
	
	public static void main(String[] args) throws ZipException, IOException, InterruptedException{
		userFile.setFileFilter(filterZipTxt); //this must be declared in the main, issues with the file
											  //chooser not opening have occurred when run elsewhere
		GUI.buildGUI(); //build the GUI
		
		
		
		//Program directory window.
	    String[] buttons = {"Cancel", "zDictionaryForm", "zZipCracker"};
	    int rc = JOptionPane.showOptionDialog(null,
	    									  "Which program would you like to use?",
	    									  "Program Directory",
	    									  JOptionPane.WARNING_MESSAGE,
	    									  0, null, buttons, buttons[0]);
	    
	    System.out.println(rc); //Debugging for control flow on buttons.
	    //Control flow for window
	    if(rc == 2) {
	    	ZipCracker zipFile = new ZipCracker();
	    	zipFile.initCracking();
	    	zipFile.executeCracking();
	    	
	    } else if(rc == 1)
	    	System.exit(0); //build dictionary. not added as of [11/24/14]
	    else
	    	System.exit(0); //exit
	}
	
}

