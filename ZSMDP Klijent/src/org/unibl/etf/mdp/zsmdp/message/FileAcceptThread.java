package org.unibl.etf.mdp.zsmdp.message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.jasypt.util.binary.BasicBinaryEncryptor;

public class FileAcceptThread extends Thread
{
	
	ServerSocket ss;
	String name;
	String user;
	
	public FileAcceptThread(int cityPort, String filename, String user)
	{
		try {
			
			//SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			//ss = ssf.createServerSocket(cityPort);
			ss = new ServerSocket(cityPort*2);
			name = filename;
			this.user = user;
			System.out.println("Pokrenut na portu " + cityPort*2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start();
	}

	
	public void run()
	{
		
			try
			{
				Socket socket = ss.accept();
				InputStream in = socket.getInputStream();
				byte[] bytes = new byte[999999];
				int count;
				FileOutputStream fos = new FileOutputStream(new File(name));
				while((count = in.read(bytes))>0)
				{
					System.out.println("Doslo je " + count);
					BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();
					binaryEncryptor.setPassword(user);
					byte[] test = new byte[count];
					for(int i=0;i<count;i++)
						test[i] = bytes[i];
					byte[] res = binaryEncryptor.decrypt(test);
					fos.write(res, 0, res.length);
				}
					
				in.close();
				fos.close();
				socket.close();
				ss.close();
			}
			catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		
			
	}
}
