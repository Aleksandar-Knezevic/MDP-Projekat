package org.unibl.etf.mdp.zsmdp.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;

import org.unibl.etf.mdp.azsmdp.rmi.AZSRMIinterface;
import org.unibl.etf.mdp.logger.MyLogger;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

public class ReportWindowController {

	String user;
	String station;
	AZSRMIinterface rmiInterface;
	
	public static String POLICY_FILE = "policyfile.txt";
	public static int REGISTRY_NO = 1099;
	public static String STUB_NAME = "AZS";
	
	
	@FXML
	ComboBox<String> filesComboBox;
	
	@FXML
	Label chosenFileLabel;
	
	
	public void downloadButton()
	{
		try
		{
			byte[] file = rmiInterface.download(filesComboBox.getValue().split("\\.Ve")[0]);
			FileOutputStream fos = new FileOutputStream(new File(filesComboBox.getValue().split("\\.Ve")[0]));
			fos.write(file);
			fos.close();
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		
	}
	
	
	public void chooseFileButton()
	{
		try
		{
			FileChooser fileChooser = new FileChooser();
	        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("document", "*.pdf"));
	        File file = fileChooser.showOpenDialog(null);
	        if(file!=null)
	        	chosenFileLabel.setText(file.getAbsolutePath());
	        
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		
	}
	
	public void sendFileButton()
	{
		try
		{
			File file = new File(chosenFileLabel.getText());
			FileInputStream fis = new FileInputStream(file);
			byte[] fileArray = new byte[fis.available()];
			fis.read(fileArray);
			rmiInterface.upload(fileArray, new File(chosenFileLabel.getText()).getName(), user, station);
			chosenFileLabel.setText("");
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	
	public void init()
	{
		File file = new File(POLICY_FILE);
		System.setProperty("java.security.policy", file.getAbsolutePath());
		if(System.getSecurityManager()==null)
			System.setSecurityManager(new SecurityManager());
		try
		{
			Registry registry = LocateRegistry.getRegistry(REGISTRY_NO);
			rmiInterface = (AZSRMIinterface) registry.lookup(STUB_NAME);
			
			
			String[] result = rmiInterface.listStation(station).split("#");
			for(int i=0;i<result.length;i++)
				filesComboBox.getItems().add(result[i]);
			
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
}
