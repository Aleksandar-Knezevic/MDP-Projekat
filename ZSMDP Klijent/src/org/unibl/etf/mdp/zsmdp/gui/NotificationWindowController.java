package org.unibl.etf.mdp.zsmdp.gui;

import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.mdp.logger.MyLogger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class NotificationWindowController implements Initializable {
	
	@FXML
	TextField messageField;
	
	String station;
	public static int PORT;
	public static String HOST_ADDR;
	
	
	public void sendNotificationButton()
	{
		String message = station + ": " + messageField.getText();
		byte[] buffer = message.getBytes();
		try
		{
			MulticastSocket socket = new MulticastSocket();
			InetAddress address = InetAddress.getByName(HOST_ADDR);
			socket.joinGroup(address);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
			socket.send(packet);
			socket.close();
			
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		messageField.getScene().getWindow().hide();
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try
		{
			Properties props = new Properties();
			props.load(new FileInputStream("config.properties"));
			PORT = Integer.parseInt(props.getProperty("MULTICAST_PORT"));
			HOST_ADDR = props.getProperty("MULTICAST_ADDR");
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		
	}

}
