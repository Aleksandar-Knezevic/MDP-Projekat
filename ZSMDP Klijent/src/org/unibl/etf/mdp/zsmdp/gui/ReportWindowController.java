package org.unibl.etf.mdp.zsmdp.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.unibl.etf.mdp.azsmdp.rmi.AZSRMIinterface;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

public class ReportWindowController {

	String user;
	String station;
	AZSRMIinterface rmiInterface;
	
	
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
			e.printStackTrace();
			// TODO: handle exception
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
			e.printStackTrace();
			// TODO: handle exception
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
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	
	public void init()
	{
		File file = new File("policyfile.txt");
		System.setProperty("java.security.policy", file.getAbsolutePath());
		if(System.getSecurityManager()==null)
			System.setSecurityManager(new SecurityManager());
		try
		{
			Registry registry = LocateRegistry.getRegistry(1099);
			rmiInterface = (AZSRMIinterface) registry.lookup("AZS");
			
			
			String[] result = rmiInterface.listStation(station).split("#");
			for(int i=0;i<result.length;i++)
				filesComboBox.getItems().add(result[i]);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
