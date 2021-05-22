package org.unibl.etf.mdp.zsmdp.message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
			
			//SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			//ss = ssf.createServerSocket(cityPort);
			ss = new ServerSocket(cityPort);
			mc = ms;
			System.out.println("Pokrenut na portu " + cityPort);
		} catch (Exception e) {
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
				//SSLSocket socket = (SSLSocket)ss.accept();
				Socket socket = ss.accept();
				System.out.println("Connection accepted");
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
					String user = br.readLine();
					String filename = br.readLine();
					byte[] fileBytes = br.readLine().getBytes();
					FileOutputStream fos = new FileOutputStream(new File(filename));
					fos.write(fileBytes);
					Platform.runLater(() ->
					{
						mc.messageArea.appendText(user + ": je poslao fajl " + filename + "\n");
					});
					fos.close();
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
