package org.unibl.etf.mdp.zsmdp.gui;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;

import org.unibl.etf.mdp.logger.MyLogger;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NotificationWindowController {
	
	@FXML
	TextField messageField;
	
	String station;
	public static int PORT = 20000;
	public static String HOST_ADDR = "224.0.0.11";
	
	
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

}
