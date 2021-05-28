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
	String station;
	
	public MulticastMessageAcceptThread(MainWindowController contr, String station)
	{
		mwc = contr;
		try
		{
			this.station=station;
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
				if(!station.equals(received.split(":")[0]))
				{
					Platform.runLater(()->
					{
						mwc.circle.setVisible(true);
						mwc.notificationArea.appendText(received+"\n");
					});
					sleep(500);
					Platform.runLater(()->
					{
						mwc.circle.setVisible(false);
					});
				}
					
			}
			catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}

}
