package org.unibl.etf.mdp.czsmdp.soap;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SoapLogin implements Serializable
{
	
	
	
	public String getStations()
	{
		try
		{
			String result = "";
			File file = new File("." +File.separator+"location-info");
			for(int i=0;i<file.list().length;i++)
				result+=file.list()[i]+"@";
			return result;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "error";
		}
		
	}
	
	public boolean login(String username, String password, String station)
	{
		try
		{
			List<String> accounts = Files.readAllLines(Paths.get(new File("location-info"+File.separator+station).toURI()));
			for(int i=0;i<accounts.size();i++)
			{
				String[] comp = accounts.get(i).split("#");
				if(username.equals(comp[0].trim())&&password.equals(comp[1].trim()))
					return true;
			}
			return false;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
		
	}
	
	
	public String getUsers(String station)
	{
		try
		{
			String result = "";
			List<String> accounts = Files.readAllLines(Paths.get(new File("location-info"+File.separator+station).toURI()));
			for(int i=0;i<accounts.size();i++)
			{
				String[] comp = accounts.get(i).split("#");
				result+=comp[0]+"#";
			}
			return result;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "error";
		}
		
	}

}
