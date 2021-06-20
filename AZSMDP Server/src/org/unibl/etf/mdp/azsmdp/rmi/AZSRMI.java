package org.unibl.etf.mdp.azsmdp.rmi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import com.google.gson.Gson;

public class AZSRMI implements AZSRMIinterface{
	
	public static final String POLICY_FILENAME = "policyfile.txt";
	public static final int REGISTRY_NO = 1099;
	public static final String STUB_NAME = "AZS";
	public static final String FILES_LOCATION = "files";

	public static void main(String[] args){
		
		try
		{
			MyLogger.setup();
			File file = new File(POLICY_FILENAME);
			System.setProperty("java.security.policy", file.getAbsolutePath());
			if(System.getSecurityManager()==null)
				System.setSecurityManager(new SecurityManager());
			AZSRMI server = new AZSRMI();
			AZSRMIinterface stub = (AZSRMIinterface) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(REGISTRY_NO);
			registry.rebind(STUB_NAME, stub);
			System.out.println("Server started.");
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		
	}

	@Override
	public void upload(byte[] fileStream, String filename, String user, String station) throws RemoteException {
		try
		{
			String finalFileName = station + filename + System.currentTimeMillis()+".pdf";
			File file = new File(FILES_LOCATION+File.separator + finalFileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(fileStream);
			fos.close();
			FileInfo fileInfo = new FileInfo(user, new SimpleDateFormat("dd.MM.yyy").format(new Date()), fileStream.length);
			Gson gson = new Gson();
			String json = gson.toJson(fileInfo);
			File jsonFile = new File(FILES_LOCATION + File.separator+finalFileName+".json");
			BufferedWriter bw = new BufferedWriter(new FileWriter(jsonFile));
			bw.write(json);
			bw.close();
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		
		
	}

	@Override
	public byte[] download(String name) throws RemoteException {
		try
		{
			File file = new File(FILES_LOCATION + File.separator + name);
			FileInputStream fis = new FileInputStream(file);
			return fis.readAllBytes();
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
			return new byte[1];
		}
		
	}

	@Override
	public String listStation(String station) throws RemoteException {
		Gson gson = new Gson();
		File file = new File(FILES_LOCATION);
		String[] allFiles = file.list();
		String result = "";
		for(int i=0;i<allFiles.length;i++)
		{
			if(allFiles[i].endsWith("pdf") && allFiles[i].startsWith(station))
			{
				try
				{
					FileInfo info = gson.fromJson(Files.readAllLines(Paths.get(new File(FILES_LOCATION + File.separator + allFiles[i]+".json").getAbsolutePath())).get(0), FileInfo.class);

					result+=allFiles[i]+".Velicina: "+info.size+"B" + "#";
				}
				catch (Exception e) {
					MyLogger.log(Level.WARNING, e.getMessage(), e);
				}
				
			}
				
		}
		return result;
	}

	@Override
	public String listAll() throws RemoteException {
		Gson gson = new Gson();
		File file = new File(FILES_LOCATION);
		String[] allFiles = file.list();
		String result = "";
		for(int i=0;i<allFiles.length;i++)
		{
			if(allFiles[i].endsWith("pdf"))
			{
				try
				{
					FileInfo info = gson.fromJson(Files.readAllLines(Paths.get(new File(FILES_LOCATION + File.separator + allFiles[i]+".json").getAbsolutePath())).get(0), FileInfo.class);
					result+=allFiles[i]+".Velicina: "+info.size+"B" + "#";
				}
				catch (Exception e) {
					MyLogger.log(Level.WARNING, e.getMessage(), e);
				}
				
			}
		}
		return result;
	}

}
