package org.unibl.etf.mdp.czsmdp.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class LinijaRedisServis {

	private static JedisPool jedisPool;
	private static Gson gson;
	
	static {
		jedisPool = new JedisPool("localhost", 6379);
		gson = new Gson();
	}
	
	public static void add(Linija linija)
	{
		System.out.println("Uslo u add");
		try(Jedis jedis = jedisPool.getResource())
		{
			if(jedis.get(linija.nazivLinije)==null)
			{
				System.out.println("Linija dodata");
				jedis.set(linija.nazivLinije, gson.toJson(linija));
				jedis.save();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	
	public static List<Linija> getAllAdmin()
	{
		try(Jedis jedis = jedisPool.getResource())
		{
			List<Linija> sveLinije = new ArrayList<Linija>();
			Set<String> keys = jedis.keys("*");
			keys.forEach(e ->
			{
				sveLinije.add(gson.fromJson(jedis.get(e), Linija.class));
			});
			return sveLinije;
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return null;
		}
	}
	
	
	public static void delete(String linija)
	{
		try(Jedis jedis = jedisPool.getResource())
		{
			jedis.del(linija);
			jedis.save();
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
}
