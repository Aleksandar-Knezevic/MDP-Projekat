package org.unibl.etf.mdp.zsmdp.gui;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.unibl.etf.mdp.azsmdp.rmi.AZSRMIinterface;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class ReportWindowController {

	String user;
	String station;
	AZSRMIinterface rmiInterface;
	
	
	@FXML
	ComboBox<String> filesComboBox;
	
	
	public void downloadButton()
	{
		
	}
	
	
	public void chooseFileButton()
	{
		
	}
	
	public void sendFileButton()
	{
		
	}
	
	
	public void init()
	{
		System.setProperty("java.security.property", "policyfile.txt");
		if(System.getSecurityManager()==null)
			System.setSecurityManager(new SecurityManager());
		try
		{
			Registry registry = LocateRegistry.getRegistry(1099);
			rmiInterface = (AZSRMIinterface) registry.lookup("AZS");
			
			
			String[] result = rmiInterface.listAll().split("#");
			for(int i=0;i<result.length;i++)
				filesComboBox.getItems().add(result[i]);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
