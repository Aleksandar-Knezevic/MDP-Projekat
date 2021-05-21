package org.unibl.etf.mdp.zsmdp.gui;

import java.io.BufferedWriter;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	
	
	public void SendMessageButton()
	{
		String message = messageBox.getText();
		messageBox.clear();
		int destPort = locationPortMapping.get(grad);
		try(Socket socket = new Socket("127.0.0.1", destPort))
		{
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
	
	
	
	
	
	
	
	
	
	









	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//		if(port>0)
//			new MessageAcceptThread(this, port);
//		if(gradovi!=null)
//			gradoviComboBox.setItems(gradovi);
//		if(grad!=null)
//			gradoviComboBox.setValue(grad);
		
		
		
	}
	
	
	public void init()
	{
		new MessageAcceptThread(this, port);
	}

}
