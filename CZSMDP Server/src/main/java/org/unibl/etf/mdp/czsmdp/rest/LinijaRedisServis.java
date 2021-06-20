package org.unibl.etf.mdp.czsmdp.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.unibl.etf.mdp.logger.MyLogger;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class LinijaRedisServis {

	private static JedisPool jedisPool;
	private static Gson gson;
	private static final String HOST = "localhost";
	private static final int PORT = 6379;
	
	static {
		jedisPool = new JedisPool(HOST, PORT);
		gson = new Gson();
		try {
			MyLogger.setup();
		} catch (IOException e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	public static void add(Linija linija)
	{
		try(Jedis jedis = jedisPool.getResource())
		{
			if(jedis.get("linija:"+linija.nazivLinije)==null)
			{
				jedis.set("linija:"+linija.nazivLinije, gson.toJson(linija));
				jedis.save();
			}
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	public static void update(Linija linija)
	{
		try(Jedis jedis = jedisPool.getResource())
		{
				jedis.set("linija:"+linija.nazivLinije, gson.toJson(linija));
				jedis.save();
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	
	public static List<Linija> getAllAdmin()
	{
		try(Jedis jedis = jedisPool.getResource())
		{
			List<Linija> sveLinije = new ArrayList<Linija>();
			Set<String> keys = jedis.keys("linija*");
			keys.forEach(e ->
			{
				sveLinije.add(gson.fromJson(jedis.get(e), Linija.class));
			});
			return sveLinije;
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}
	
	
	public static void delete(String linija)
	{
		try(Jedis jedis = jedisPool.getResource())
		{
			jedis.del("linija:"+linija);
			jedis.save();
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	public static List<Linija> getByStation(String nazivLinije)
	{
		
		return getAllAdmin().stream().filter(e -> e.vozProlazakMapa.keySet().contains(nazivLinije)).collect(Collectors.toList());
	}
	
}
