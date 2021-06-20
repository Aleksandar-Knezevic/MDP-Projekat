package org.unibl.etf.mdp.messages;

import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Properties;
import java.util.logging.Level;

import org.unibl.etf.mdp.czsmdpclient.gui.MainWindowController;
import org.unibl.etf.mdp.logger.MyLogger;

import javafx.application.Platform;

public class MulticastAcceptThread extends Thread {

	
	public MainWindowController mwc;
	int port;
	String host;
	MulticastSocket socket;
	
	public MulticastAcceptThread(MainWindowController m)
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
		mwc=m;
		try
		{
			socket = new MulticastSocket(port);
			InetAddress address = InetAddress.getByName(host);
			socket.joinGroup(address);
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		start();
	}
	
	@Override
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
				MyLogger.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}
}
