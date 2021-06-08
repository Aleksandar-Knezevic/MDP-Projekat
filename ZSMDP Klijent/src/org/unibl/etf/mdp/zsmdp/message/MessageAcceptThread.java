package org.unibl.etf.mdp.zsmdp.message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

import org.jasypt.util.text.BasicTextEncryptor;
import org.unibl.etf.mdp.logger.MyLogger;
import org.unibl.etf.mdp.zsmdp.gui.MainWindowController;

import javafx.application.Platform;


public class MessageAcceptThread extends Thread
{
	ServerSocket ss;
	MainWindowController mc;
	int port;
	
	public MessageAcceptThread(MainWindowController ms, int cityPort)
	{
		try {

			port = cityPort;
			ss = new ServerSocket(cityPort);
			mc = ms;
			System.out.println("Pokrenut na portu " + cityPort);
		} catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		start();
	}
	
	
	public void run()
	{
		while(true)
		{
			try {
				Socket socket = ss.accept();
				System.out.println("Connection accepted");
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
				String type = br.readLine();
				if("MSG".equals(type))
				{
					String incomingMessage = br.readLine();
					String[] parts = incomingMessage.split(":");
					BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
					textEncryptor.setPassword(parts[0]);
					String message = textEncryptor.decrypt(parts[1]);
					Platform.runLater(() ->
					{
						mc.messageArea.appendText(parts[0]+": "+message+'\n');
					});
				}
				if("FILE".equals(type))
				{
					String user = br.readLine();
					String fileName = br.readLine();
					new FileAcceptThread(port, fileName, user);
					Platform.runLater(() ->
					{
						mc.messageArea.appendText(user + " je poslao fajl: " + fileName+"\n");
					});
				}
				
			
				br.close();
				socket.close();
			} catch (Exception e) {
				MyLogger.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}

}
