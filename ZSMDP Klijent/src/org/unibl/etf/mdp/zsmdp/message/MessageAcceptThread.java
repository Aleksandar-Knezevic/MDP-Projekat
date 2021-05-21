package org.unibl.etf.mdp.zsmdp.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.unibl.etf.mdp.zsmdp.gui.MainWindowController;

import javafx.application.Platform;


public class MessageAcceptThread extends Thread
{
	ServerSocket ss;
	MainWindowController mc;
	
	public MessageAcceptThread(MainWindowController ms, int cityPort)
	{
		try {
			
			ss = new ServerSocket(cityPort);
			mc = ms;
			System.out.println("Pokrenut na portu " + cityPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start();
	}
	
	
	public void run()
	{
		while(true)
		{
			try {
				Socket socket = ss.accept();
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String type = br.readLine();
				if("MSG".equals(type))
				{
					String message = br.readLine();
					Platform.runLater(() ->
					{
						mc.messageArea.appendText(message+'\n');
					});
				}
				else if("FILE".equals(type))
				{
					
				}
				else
				{
					//error
				}
				br.close();
				socket.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
