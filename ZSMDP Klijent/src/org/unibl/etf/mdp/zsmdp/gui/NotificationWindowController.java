package org.unibl.etf.mdp.zsmdp.gui;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NotificationWindowController {
	
	@FXML
	TextField messageField;
	
	String station;
	
	
	public void sendNotificationButton()
	{
		String message = station + ": " + messageField.getText();
		int port = 20000;
		String host = "224.0.0.11";
		byte[] buffer = message.getBytes();
		try
		{
			MulticastSocket socket = new MulticastSocket();
			InetAddress address = InetAddress.getByName(host);
			socket.joinGroup(address);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
			socket.send(packet);
			socket.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		messageField.getScene().getWindow().hide();
	}

}
