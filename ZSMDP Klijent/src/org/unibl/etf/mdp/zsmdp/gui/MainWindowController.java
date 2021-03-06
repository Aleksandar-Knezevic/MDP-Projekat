package org.unibl.etf.mdp.zsmdp.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import org.jasypt.util.binary.BasicBinaryEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.unibl.etf.mdp.czsmdp.soap.SoapLogin;
import org.unibl.etf.mdp.czsmdp.soap.SoapLoginServiceLocator;
import org.unibl.etf.mdp.logger.MyLogger;
import org.unibl.etf.mdp.zsmdp.message.MessageAcceptThread;
import org.unibl.etf.mdp.zsmdp.message.MulticastMessageAcceptThread;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindowController{
	
	public int port;
	public String grad;
	public HashMap<String, Integer> locationPortMapping;
	public String korisnik;
	public static String HOST_ADDR;


	@FXML
	public TextArea messageArea;
	@FXML
	public TextField messageBox;
	@FXML
	public ComboBox<String> gradoviComboBox;
	@FXML
	public Label chosenFileLabel;
	@FXML
	public TextArea notificationArea;
	@FXML
	public Circle circle;
	@FXML
	public ComboBox<String> korisniciComboBox;
	@FXML
	public Button sendMessageButton;
	@FXML
	public Button sendFileButton;
	
	
	public void SendMessageButton()
	{
		String plainMessage = messageBox.getText();
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(korisnik);
		String message = textEncryptor.encrypt(plainMessage);
		System.out.println(message);
		messageBox.clear();
		int destPort = locationPortMapping.get(gradoviComboBox.getValue());
		try(Socket socket =  new Socket(HOST_ADDR, destPort))
		{
			System.out.println("Connection established with port " + destPort);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			pw.println("MSG");
			pw.println(korisnik+":"+message);
			pw.close();
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	
	public void sendReport()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportWindow.fxml"));
			Parent root = loader.load(); 
			ReportWindowController rwc =  loader.<ReportWindowController>getController();
			
			rwc.station = grad;
			rwc.user = korisnik;
			rwc.init();
			
			
			Scene scene = new Scene(root,350,175);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Report");
			stage.show();
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	
	
	public void displayTimetable()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TimetableWindow.fxml"));
			Parent root = loader.load(); 
			
			TimetableWindowController twc = loader.<TimetableWindowController>getController();
			twc.station = grad;
			
			twc.init();
			
			
			Scene scene = new Scene(root,420,550);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Timetable");
			stage.show();
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	public void recordTransit()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordTransitWindow.fxml"));
			Parent root = loader.load(); 
			
			RecordTransitWindowController rtwc = loader.<RecordTransitWindowController>getController();
			rtwc.station = grad;
			rtwc.init();
			
			Scene scene = new Scene(root,310,140);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Record transit");
			stage.show();
			
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
	        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("all", "*.*"));
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
			byte[] plainFileArray = new byte[fis.available()];
			fis.read(plainFileArray);
			chosenFileLabel.setText("");
			int destPort = locationPortMapping.get(gradoviComboBox.getValue());
			Socket socket =  new Socket(HOST_ADDR, destPort);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			pw.println("FILE");
			pw.println(korisnik);
			pw.println(file.getName());
			pw.close();
			socket.close();
			fis.close();
			Thread.sleep(100);
			
			System.out.println("Connection established with port " + destPort);
			BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();
			binaryEncryptor.setPassword(korisnik.trim());
			byte[] fileArray = binaryEncryptor.encrypt(plainFileArray);
			Socket socket1 = new Socket(HOST_ADDR, destPort*2);
			OutputStream out = socket1.getOutputStream();
			out.write(fileArray, 0, fileArray.length);
			out.close();
			socket1.close();
			
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
		
	
	
	
	public void sendNotificationButton()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("NotificationWindow.fxml"));
			Parent root = loader.load(); 
			
			NotificationWindowController nwc = loader.<NotificationWindowController>getController();
			
			//twc.init();
			nwc.station = grad;
			
			
			Scene scene = new Scene(root,360,165);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Send notification");
			stage.show();
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	
	public void logout()
	{
		try
		{
			SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
			SoapLogin login = locator.getSoapLogin();
			login.logout(grad+"#"+locationPortMapping.get(grad)+".xml");
			messageArea.getScene().getWindow().hide();
			System.exit(0);
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		
	}
	
	
	public void getUsers()
	{
		try
		{
			SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
			SoapLogin login = locator.getSoapLogin();
			if(!gradoviComboBox.getValue().equals(grad))
			{
				korisniciComboBox.getItems().clear();
				korisniciComboBox.getItems().add(login.getOnlineUsers(gradoviComboBox.getValue()+"#"+locationPortMapping.get(gradoviComboBox.getValue())+".xml"));
			}
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	
	public void comboAction(ActionEvent event)
	{
		if(korisniciComboBox.getValue()!=null)
		{
			sendFileButton.setDisable(false);
			sendMessageButton.setDisable(false);
		}
		else
		{
			sendFileButton.setDisable(true);
			sendMessageButton.setDisable(true);
		}
		
	}
	
	
	
	









	
	
	
	public void init()
	{
		try
		{
			Properties props = new Properties();
			props.load(new FileInputStream("config.properties"));
			HOST_ADDR = props.getProperty("HOST");
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		new MessageAcceptThread(this, port);
		new MulticastMessageAcceptThread(this, grad);
	}

}
