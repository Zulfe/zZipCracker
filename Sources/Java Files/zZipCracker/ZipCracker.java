package zZipCracker2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;



public class ZipCracker {
	
	public ZipCracker(String fileName){
		System.out.println(fileName);
	}
	
	public ZipCracker() {
		// TODO Auto-generated constructor stub
	}
	

	//cracking related
	private static String fileName;
	private static ZipFile zipper;
	private static String dest;
	private static String txtFile;
	private static int passwordCounter;
	private static String currentPassword;
	private static BufferedReader br;
	private static JFileChooser userFile = Main.userFile;
	//private static FileNameExtensionFilter filterZipTxt = new FileNameExtensionFilter("Zip Files", "zip", "txt"); //uncomment and use if you have issues with filters in main

	//GUI shorthands
	private static String uT;
	private static JTextArea dis = GUI.display;
	private static JFrame f = GUI.frame;

	//control flow related
	private static boolean userContinue = true;
	private static boolean userCrack = true;
	
	public void initCracking() throws ZipException, FileNotFoundException{
		fileName = filePick(); //get the file's path using the file chooser GUI
		System.out.println(fileName); //used for debugging
		zipper = new ZipFile(fileName); //instantiate the zip as a zip4j ZipFile object
		
		dest = JOptionPane.showInputDialog("Please input a destination for the unzipped file." + "\n" +
											"Triple check the path, as this program WILL overwrite directories.");
		
		JOptionPane.showMessageDialog(null, "You will now choose your dictionary.");
		txtFile = filePick();
		br = new BufferedReader(new FileReader(txtFile));
	}
	
	//starts the outer shell of the cracking process
	public void executeCracking() throws ZipException, IOException, InterruptedException{
		f.setVisible(true);
		while(userContinue == true){
			inputHandlerOuter();
			processLine();
		}
	}
	
	public void processLine() throws ZipException, IOException, InterruptedException{
		if(zipper.isEncrypted()) {
			while(userCrack == true) { //while the user has not run a command to stop the loop
				String line;
	            if ((line = br.readLine()) != null) {
	            	currentPassword = line;
	                if(currentPassword.length() == 0 || currentPassword.contains("  ")){
	                	System.out.println(currentPassword);
	                	readyAttempt("nullTest");
	                } else{
	                    readyAttempt(currentPassword);
	                }
	                
	            }
			}
			  //if the zip doesn't have a password, just open it. tell user that the zip is not locked
        } 
		else {
            zipper.extractAll(dest);
            JOptionPane.showMessageDialog(null, "The selected zip was not password protected. It was extracted anyways.");
        }

}	
	
	public static void readyAttempt(String currentPassword) throws ZipException, IOException, InterruptedException{
		zipper.setPassword(currentPassword);
		System.out.println("Testing password no. " + passwordCounter + ", which is " + currentPassword);
		windowControl(currentPassword);
		passwordCounter = passwordCounter + 1;
		crackAttempt(currentPassword);
	}
	
	public static void crackAttempt(String currentPassword) throws IOException, InterruptedException{
		try {
			zipper.extractAll(dest);
			br.close();
			JOptionPane.showMessageDialog(null, "The zip has been cracked. The password is " + currentPassword);
			System.exit(0);
			
			 //if a ZipException (unique to the Zip4j library) occurs, suppress it, remove the
			 //password that doesn't work, check for user commands, and move to next password
		} 
		catch(ZipException ze) {
			//zip has not been cracked, go to next line
		}
		inputHandlerInner();
		}
	
	public static void windowControl(String currentPassword){
		//GUI display printing, row number control, and debugging
		String windowText = "Testing password no." + passwordCounter + ", which is " + currentPassword;
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
		uT = GUI.userText;
		if(uT.equals("STOP")){
			userCrack = false;
			dis.append("PROGRAM HAS BEEN PAUSED");
			GUI.userInput.setText("");
		}
		else if(GUI.userText.equals("KILL")){
			System.exit(0);
		}
	}
	
	//out-loop commands
	public static void inputHandlerOuter(){
		//user input handler
		uT = GUI.userText;
		if(uT.equals("CONTINUE") && userCrack == false){
			userCrack = true;
			dis.append("PROGRAM HAS BEEN CONTINUED");
			GUI.userInput.setText("");
		}
		else if (GUI.userText.equals("KILL") && userCrack == false)
			System.exit(0);
	}
	
	
	
	
}
