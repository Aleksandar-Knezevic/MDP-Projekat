package org.unibl.etf.mdp.zsmdp.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.mdp.czsmdp.soap.SoapLogin;
import org.unibl.etf.mdp.czsmdp.soap.SoapLoginServiceLocator;
import org.unibl.etf.mdp.logger.MyLogger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	
	@FXML
	Button loginButton;
	
	@FXML
	ComboBox<String> loginGradoviBox;
	
	@FXML
	TextField usernameField;
	
	@FXML
	PasswordField passwordField;
	
	@FXML
	Label errorLabel;
	
	private HashMap<String, Integer> locationPortMapping;
	
	public void LoginButton()
	{
		Stage thisStage = (Stage)loginButton.getScene().getWindow();
		
		
		try
		{
			String grad = loginGradoviBox.getValue().trim();
			String username = usernameField.getText().trim();
			String password = passwordField.getText().trim();
			
			
			SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
			SoapLogin login = locator.getSoapLogin();
			
			String station = grad+"#"+locationPortMapping.get(grad)+".xml";
			
			if(login.login(username, password, station))
			{
				ObservableList<String> gradovi = loginGradoviBox.getItems();
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
				Parent root = loader.load(); 
				
				
				MainWindowController mwc = loader.<MainWindowController>getController();
				
				mwc.grad = grad;
				mwc.port = locationPortMapping.get(grad);
				mwc.locationPortMapping= locationPortMapping;
				mwc.korisnik = username;
				mwc.gradoviComboBox.getItems().addAll(loginGradoviBox.getItems());
				mwc.gradoviComboBox.setValue(grad);
				mwc.init();
				thisStage.hide();
				
				
				
				Scene scene = new Scene(root,600,400);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setTitle(grad);
				stage.show();
			}
			else
			{
				errorLabel.setVisible(true);
			}
			
			
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		locationPortMapping = new HashMap<String, Integer>();
		SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
		try {
		SoapLogin login = locator.getSoapLogin();
		String allLocations = login.getStations();
		String[] portLocations = allLocations.split("@");
		for(int i=0;i<portLocations.length;i++)
		{
			String[] split = portLocations[i].split("#");
			locationPortMapping.put(split[0], Integer.parseInt(split[1].split("\\.")[0]));
		}
		
		} catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		loginGradoviBox.getItems().addAll(locationPortMapping.keySet());
		
	}
	
}
