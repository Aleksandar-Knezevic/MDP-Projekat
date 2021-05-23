package org.unibl.etf.mdp.zsmdp.message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileAcceptThread extends Thread
{
	
	ServerSocket ss;
	String name;
	
	public FileAcceptThread(int cityPort, String filename)
	{
		try {
			
			//SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			//ss = ssf.createServerSocket(cityPort);
			ss = new ServerSocket(cityPort*2);
			name = filename;
			System.out.println("Pokrenut na portu " + cityPort*2);
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
			try
			{
				Socket socket = ss.accept();
				InputStream in = socket.getInputStream();
				byte[] bytes = new byte[1024];
				int count;
				FileOutputStream fos = new FileOutputStream(new File(name));
				while((count = in.read(bytes))>0)
					fos.write(bytes, 0, bytes.length);
			}
			catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
			
	}
}
