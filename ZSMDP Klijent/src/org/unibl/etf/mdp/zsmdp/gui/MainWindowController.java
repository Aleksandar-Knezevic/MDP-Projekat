package org.unibl.etf.mdp.zsmdp.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.unibl.etf.mdp.zsmdp.message.MessageAcceptThread;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindowController implements Initializable{
	
	public int port;
	public String grad;
	public HashMap<String, Integer> locationPortMapping;
	public String korisnik;


	@FXML
	public TextArea messageArea;
	@FXML
	public TextField messageBox;
	@FXML
	public ComboBox<String> gradoviComboBox;
	@FXML
	public Label chosenFileLabel;
	
	
	public void SendMessageButton()
	{
		String message = messageBox.getText();
		messageBox.clear();
		int destPort = locationPortMapping.get(gradoviComboBox.getValue());
		//SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		//try(SSLSocket socket = (SSLSocket) sf.createSocket("127.0.0.1", destPort))
		try(Socket socket =  new Socket("127.0.0.1", destPort))
		{
			System.out.println("Connection established with port " + destPort);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			pw.println("MSG");
			pw.println(korisnik+":"+message);
			pw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
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
			// TODO: handle exception
			e.printStackTrace();
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
			
			//twc.init();
			
			
			Scene scene = new Scene(root,420,550);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Timetable");
			stage.show();
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
	        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("all", "*.*"));
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
			chosenFileLabel.setText("");
			int destPort = locationPortMapping.get(gradoviComboBox.getValue());
			Socket socket =  new Socket("127.0.0.1", destPort);
			
			System.out.println("Connection established with port " + destPort);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			pw.println("FILE");
			pw.println(korisnik);
			pw.println(file.getName());
			pw.println(fileArray.toString());
			pw.close();
			socket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
		
	
	
	
	
	
	
	
	
	
	









	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		File file = new File("keystore.jks");
		System.setProperty("javax.net.ssl.keyStore", file.getAbsolutePath());
		System.setProperty("javax.net.ssl.keyStorePassword", "securemdp");
		System.setProperty("javax.net.ssl.trustStore", file.getAbsolutePath());
		System.setProperty("javax.net.ssl.trustStorePassword", "securemdp");
		
	}
	
	
	public void init()
	{
		new MessageAcceptThread(this, port);
	}

}
