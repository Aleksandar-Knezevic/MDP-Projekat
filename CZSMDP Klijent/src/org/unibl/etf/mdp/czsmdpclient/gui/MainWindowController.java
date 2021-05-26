package org.unibl.etf.mdp.czsmdpclient.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.unibl.etf.mdp.azsmdp.rmi.AZSRMIinterface;
import org.unibl.etf.mdp.czsmdp.soap.SoapLogin;
import org.unibl.etf.mdp.czsmdp.soap.SoapLoginServiceLocator;
import org.unibl.etf.mdp.messages.MulticastAcceptThread;

import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
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
	@FXML
	public ComboBox<String> filesComboBox;
	@FXML
	public TextArea notificationArea;
	@FXML
	public TextField nazivLinijePolje;
	@FXML
	public TextField redVoznjePolje;
	
	
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
		getUsers();
		if(!korisniciBox.getItems().contains(usernameField.getText()))
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
		
	}
	
	
	public void deleteUser()
	{
		
		String station = gradoviBox.getValue().trim() + "#" + locationPortMapping.get(gradoviBox.getValue())+".xml";
		String user = korisniciBox.getValue();
		try
		{
			SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
			SoapLogin login = locator.getSoapLogin();
			login.deleteUser(user, station);
			getUsers();
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	
	
	
	public void getReports()
	{
		File file = new File("policyfile.txt");
		System.setProperty("java.security.policy", file.getAbsolutePath());
		if(System.getSecurityManager()==null)
			System.setSecurityManager(new SecurityManager());
		try
		{
			Registry registry = LocateRegistry.getRegistry(1099);
			AZSRMIinterface rmiInterface = (AZSRMIinterface) registry.lookup("AZS");
			
			
			String[] result = rmiInterface.listAll().split("#");
			for(int i=0;i<result.length;i++)
				filesComboBox.getItems().add(result[i]);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	public void downloadReport()
	{
		try
		{
			Registry registry = LocateRegistry.getRegistry(1099);
			AZSRMIinterface rmiInterface = (AZSRMIinterface) registry.lookup("AZS");
			byte[] file = rmiInterface.download(filesComboBox.getValue());
			FileOutputStream fos = new FileOutputStream(new File(filesComboBox.getValue()));
			fos.write(file);
			fos.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	
	public void addTimetable()
	{
		try
		{
			String nazivLinije = nazivLinijePolje.getText();
			String redVoznje = redVoznjePolje.getText();
			Linija linija = new Linija(nazivLinije, redVoznje);
			Gson gson = new Gson();
			String res = gson.toJson(linija);
			URL url = new URL("http://localhost:8080/CZSMDPServer/api/rest/stations/admin/add");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setDoOutput(true);
			try(OutputStream os = con.getOutputStream()) {
			    byte[] input = res.getBytes("utf-8");
			    os.write(input, 0, input.length);		
			    
			}
			nazivLinijePolje.setText("");
			redVoznjePolje.setText("");
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
		getReports();
		new MulticastAcceptThread(this);
		
	}

}
