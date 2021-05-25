package org.unibl.etf.mdp.czsmdpclient.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.unibl.etf.mdp.czsmdp.soap.SoapLogin;
import org.unibl.etf.mdp.czsmdp.soap.SoapLoginServiceLocator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class MainWindowController implements Initializable{

	public HashMap<String, Integer> locationPortMapping;
	
	
	@FXML
	public ComboBox<String> gradoviBox;
	@FXML
	public ComboBox<String> korisniciBox;
	@FXML
	public TextField usernameField;
	@FXML
	public TextField passwordField;
	
	
	public void getUsers()
	{
		SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
		try {
		SoapLogin login = locator.getSoapLogin();
		String station = gradoviBox.getValue().trim() + "#" + locationPortMapping.get(gradoviBox.getValue())+".xml";
		String[] users = login.getUsers(station).split("#");
		korisniciBox.getItems().clear();
		for(int i=0;i<users.length;i++)
			korisniciBox.getItems().add(users[i]);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	public void addUser()
	{
		SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
		try {
		SoapLogin login = locator.getSoapLogin();
		String station = gradoviBox.getValue().trim() + "#" + locationPortMapping.get(gradoviBox.getValue())+".xml";
		login.addUser(usernameField.getText().trim(), passwordField.getText().trim(), station);
		
		usernameField.setText("");
		passwordField.setText("");
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	
	public void deleteUser()
	{
		String station = gradoviBox.getValue().trim() + "#" + locationPortMapping.get(gradoviBox.getValue())+".xml";
		String user = korisniciBox.getValue();
		try
		{
			SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
			SoapLogin login = locator.getSoapLogin();
			//login.deleteUser(user, station);
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		locationPortMapping = new HashMap<String, Integer>();
		SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
		try {
		SoapLogin login = locator.getSoapLogin();
		String allLocations = login.getStations();
		//System.out.println(allLocations);
		String[] portLocations = allLocations.split("@");
		for(int i=0;i<portLocations.length;i++)
		{
			String[] split = portLocations[i].split("#");
			locationPortMapping.put(split[0], Integer.parseInt(split[1].split("\\.")[0]));
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gradoviBox.getItems().addAll(locationPortMapping.keySet());
		
	}

}
