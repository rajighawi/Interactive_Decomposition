package utilities;
 

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import main.Global;

public class FileUtility {
	

	public static void save(String textToSave){
		JFileChooser fileChooser = Global.getInstance().getFileChooser();
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			saveToFile(textToSave, file);		
		}
	}
	
	public static String load(){
		JFileChooser fileChooser = Global.getInstance().getFileChooser();
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return readUTFFile(file);		
		}
		return null;
	}
 
	public static boolean saveToFile(String textToSave, File file){		
		try{
			FileOutputStream out = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");			
			osw.write(textToSave);
			osw.close();
			out.close();
			return true;
		} catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
			JOptionPane.showMessageDialog(null, fnfe.getMessage(), "FileNotFoundException", JOptionPane.ERROR_MESSAGE);
		} catch(IOException ioe){
			ioe.printStackTrace();
			JOptionPane.showMessageDialog(null, ioe.getMessage(), "IOException", JOptionPane.ERROR_MESSAGE);
		} catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
		}					
		return false;
	}	
	
	public static String readUTFFile(File file){
		String txt = "";
		try{
			FileInputStream in = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(in, "UTF-8");	
			BufferedReader br = new BufferedReader(isr); 		 
			String s;
			while((s = br.readLine()) != null) { 
				txt += s+"\n"; 
			} 
			br.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return txt;
	}
	
	
	public static String loadFileBrowse(int fileTypeAction) {
		String filePath = null;
		JFileChooser fileChooser = Global.getInstance().getFileChooser();
		if (fileTypeAction == 3) {
			fileChooser.setDialogTitle("Open: Specify an XML Based File");			
		} else if (fileTypeAction == 2) {
			fileChooser.setDialogTitle("Open: Specify OWL Ontology  File");						
		} else if (fileTypeAction == 1) {
			fileChooser.setDialogTitle("Open: Resume a Mapping Project");						
		} else
			return null;
		int openValue = fileChooser.showOpenDialog(null);
		if (openValue == 0) {
			try {
				filePath = fileChooser.getSelectedFile().getAbsolutePath();				
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
		return filePath;
	}
	
	
	public static void saveListToFile(ArrayList<String> list, String filePath){
		File file = new File(filePath);
		try{
			FileWriter fw = new FileWriter(file);			
			for (int i = 0; i < list.size(); i++) {
				String s = list.get(i);
				fw.write(s+"\n");
				System.out.println(s);
			}			
			fw.close();
		} catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public static boolean saveToFile(String textToSave, String filePath){
		File file = new File(filePath);
		try{
			FileWriter fw = new FileWriter(file);
			fw.write(textToSave);
			fw.close();
			return true;
		} catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}					
		return false;
	}
	
	public static ArrayList<String> read(String fileName){
		ArrayList<String> list = new ArrayList<String>();
		Path path = Paths.get(fileName);				
		Charset charset = Charset.forName("UTF-8");
	//	long lineNb = 0;
		String line = null;
		try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
		   
		    while ((line = reader.readLine()) != null) {
		    //	lineNb++;
		    	list.add(line);
		    }
		//    System.out.println(lineNb);
		    reader.close();
		} catch (IOException x) {
		    x.printStackTrace();		   
		}	
		return list;
	}
	
	public static String read2Str(String fileName){
		String list = "";
		Path path = Paths.get(fileName);				
		Charset charset = Charset.forName("UTF-8");
		long lineNb = 0;
		String line = null;
		try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
		   
		    while ((line = reader.readLine()) != null) {
		    	lineNb++;
		    	list+=line+"\n";
		    }
		    System.out.println(lineNb);
		    reader.close();
		} catch (IOException x) {
		    x.printStackTrace();		   
		}	
		return list;
	}
	
	public static void openPDF(String filename){
		if (Desktop.isDesktopSupported()) {
			if(filename.endsWith(".pdf")){
				try {
					File myFile = new File(filename);
					Desktop.getDesktop().open(myFile);
				} catch (IOException ex) {
					ex.printStackTrace();		       	
				}
			}		   
		}
	}
	
}
