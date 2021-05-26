package org.unibl.etf.mdp.azsmdp.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AZSRMIinterface extends Remote {
	
	public void upload(byte[] fileStream, String filename, String user, String station) throws RemoteException;
	
	public byte[] download(String name) throws RemoteException;
	
	public String listStation(String station) throws RemoteException;
	
	public String listAll() throws RemoteException;

}
