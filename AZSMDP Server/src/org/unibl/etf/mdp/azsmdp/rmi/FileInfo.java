package org.unibl.etf.mdp.azsmdp.rmi;

public class FileInfo 
{
	String user;
	String date;
	int size;
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public FileInfo(String user, String date, int size) {
		super();
		this.user = user;
		this.date = date;
		this.size = size;
	}
	public FileInfo() {
		super();
	}
	

}
