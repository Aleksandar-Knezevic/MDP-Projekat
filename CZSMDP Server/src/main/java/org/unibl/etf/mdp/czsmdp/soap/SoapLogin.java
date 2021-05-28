package org.unibl.etf.mdp.czsmdp.soap;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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
	
	public void logout(String station)
	{
		try
		{
			JedisPool jedisPool = new JedisPool("localhost", 6379);
			Jedis jedis = jedisPool.getResource();
			jedis.del("status:"+station);
			jedis.save();
			jedisPool.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	
	
	
	
	public boolean login(String username, String password, String station)
	{
			try
			{
				JedisPool jedisPool = new JedisPool("localhost", 6379);
				Jedis jedis = jedisPool.getResource();
				if(jedis.get("status:"+station)==null)
				{
					File file = new File("." +File.separator+"location-info"+ File.separator + station);
					XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
					ArrayList<User> accounts = (ArrayList<User>) decoder.readObject();
					decoder.close();
					
					for(int i=0;i<accounts.size();i++)
					{
						String hashed = hash(password);
						User u = accounts.get(i);
						if(u.getUsername().equals(username) && u.getPassword().equals(hashed))
						{
							jedis.set("status:"+station, username);
							jedis.save();
							jedisPool.close();
							return true;
						}
							
						
					}
					jedisPool.close();
					return false;
				}
				else
				{
					jedisPool.close();
					return false;
				}
				
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
			File file = new File("." +File.separator+"location-info"+ File.separator + station);
			XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
			ArrayList<User> accounts = (ArrayList<User>) decoder.readObject();
			decoder.close();
			String result="";
			for(int i=0;i<accounts.size();i++)
			{
				
				result+=accounts.get(i).username+"#";
			}
			return result;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}
		
	}
	
	public String getOnlineUsers(String station)
	{
		try(JedisPool jedisPool = new JedisPool("localhost", 6379))
		{
			Jedis jedis = jedisPool.getResource();
			if(jedis.get("status:"+station)==null)
				return "";
			else
				return jedis.get("status:"+station);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "error";
			// TODO: handle exception
		}
		
	}
	
	
	
	public void addUser(String username, String password, String station)
	{
		try
		{
			File file = new File("." +File.separator+"location-info"+ File.separator + station);
			XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
			ArrayList<User> accounts = (ArrayList<User>) decoder.readObject();
			decoder.close();
			User u = new User(station.split("#")[0],username, hash(password));
			accounts.add(u);
			XMLEncoder encoder = new XMLEncoder(new FileOutputStream(file));
			encoder.writeObject(accounts);
			encoder.close();
		
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void deleteUser(String username, String station)
	{
		try
		{
			File file = new File("." +File.separator+"location-info"+ File.separator + station);
			XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
			ArrayList<User> accounts = (ArrayList<User>) decoder.readObject();
			decoder.close();
			for(int i=0;i<accounts.size();i++)
			{
				User u = accounts.get(i);
				if(u.username.equals(username))
				{
					accounts.remove(i);
					break;
				}
					
			}
			
			
			XMLEncoder encoder = new XMLEncoder(new FileOutputStream(file));
			encoder.writeObject(accounts);
			encoder.close();
		
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private String hash(String password) throws Exception
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		return bytesToHex(encodedHash);
		
	}
	
	private String bytesToHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder(2 * hash.length);
	    for (int i = 0; i < hash.length; i++) {
	        String hex = Integer.toHexString(0xff & hash[i]);
	        if(hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
	


}
