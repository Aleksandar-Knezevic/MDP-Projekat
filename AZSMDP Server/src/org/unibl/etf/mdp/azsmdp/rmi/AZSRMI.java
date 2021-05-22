package org.unibl.etf.mdp.azsmdp.rmi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

public class AZSRMI implements AZSRMIinterface{

	public static void main(String[] args) throws Exception{
		File file = new File("policyfile.txt");
		System.setProperty("java.security.policy", file.getAbsolutePath());
		if(System.getSecurityManager()==null)
			System.setSecurityManager(new SecurityManager());
		AZSRMI server = new AZSRMI();
		AZSRMIinterface stub = (AZSRMIinterface) UnicastRemoteObject.exportObject(server, 0);
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.rebind("AZS", stub);
		System.out.println("Server started.");
	}

	@Override
	public void upload(byte[] fileStream, String filename, String user, String station) throws RemoteException {
		try
		{
			String finalFileName = station + filename + System.currentTimeMillis()+".pdf";
			File file = new File("files"+File.separator + finalFileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(fileStream);
			fos.close();
			FileInfo fileInfo = new FileInfo(user, new SimpleDateFormat("dd.MM.yyy").format(new Date()), fileStream.length);
			Gson gson = new Gson();
			String json = gson.toJson(fileInfo);
			File jsonFile = new File("files" + File.separator+finalFileName+".json");
			BufferedWriter bw = new BufferedWriter(new FileWriter(jsonFile));
			bw.write(json);
			bw.close();
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}

	@Override
	public byte[] download(String name) throws RemoteException {
		try
		{
			File file = new File("files" + File.separator + name);
			FileInputStream fis = new FileInputStream(file);
			return fis.readAllBytes();
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new byte[1];
		}
		
	}

	@Override
	public String listStation(String station) throws RemoteException {
		File file = new File("files");
		String[] allFiles = file.list();
		String result = "";
		for(int i=0;i<allFiles.length;i++)
		{
			if(allFiles[i].endsWith("pdf") && allFiles[i].startsWith(station))
				result+=allFiles[i]+"#";
		}
		return result;
	}

	@Override
	public String listAll() throws RemoteException {
		File file = new File("files");
		String[] allFiles = file.list();
		String result = "";
		for(int i=0;i<allFiles.length;i++)
		{
			if(allFiles[i].endsWith("pdf"))
				result+=allFiles[i]+"#";
		}
		return result;
	}

}
