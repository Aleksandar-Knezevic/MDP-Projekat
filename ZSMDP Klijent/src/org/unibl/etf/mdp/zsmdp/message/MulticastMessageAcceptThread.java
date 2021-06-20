package org.unibl.etf.mdp.zsmdp.message;

import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Properties;
import java.util.logging.Level;

import org.unibl.etf.mdp.logger.MyLogger;
import org.unibl.etf.mdp.zsmdp.gui.MainWindowController;

import javafx.application.Platform;

public class MulticastMessageAcceptThread extends Thread
{
	
	MainWindowController mwc;
	int port;
	String host;
	MulticastSocket socket;
	String station;
	
	public MulticastMessageAcceptThread(MainWindowController contr, String station)
	{
		mwc = contr;
		try
		{
			try
			{
				Properties props = new Properties();
				props.load(new FileInputStream("config.properties"));
				port = Integer.parseInt(props.getProperty("MULTICAST_PORT"));
				host = props.getProperty("MULTICAST_ADDR");
			}
			catch (Exception e) {
				MyLogger.log(Level.WARNING, e.getMessage(), e);
			}
			this.station=station;
			socket = new MulticastSocket(port);
			InetAddress address = InetAddress.getByName(host);
			socket.joinGroup(address);
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
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
				MyLogger.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}

}
