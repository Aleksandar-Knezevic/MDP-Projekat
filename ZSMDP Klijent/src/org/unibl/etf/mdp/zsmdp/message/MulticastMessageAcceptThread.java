package org.unibl.etf.mdp.zsmdp.message;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.unibl.etf.mdp.zsmdp.gui.MainWindowController;

import javafx.application.Platform;

public class MulticastMessageAcceptThread extends Thread
{
	
	MainWindowController mwc;
	int port = 20000;
	String host = "224.0.0.11";
	MulticastSocket socket;
	
	public MulticastMessageAcceptThread(MainWindowController contr)
	{
		mwc = contr;
		try
		{
			socket = new MulticastSocket(port);
			InetAddress address = InetAddress.getByName(host);
			socket.joinGroup(address);
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		
		start();
	}
	
	
	public void run()
	{
		while(true)
		{
			try
			{
				byte[] buffer = new byte[512];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String received = new String(packet.getData(), 0, packet.getLength());
				Platform.runLater(()->
				{
					mwc.notificationArea.appendText(received+"\n");
				});
			}
			catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}

}
