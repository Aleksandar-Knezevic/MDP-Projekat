package org.unibl.etf.mdp.czsmdp.rest;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/stations")
public class StationApi {

	
	@GET
	@Path("/{city}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Linija> getAll(@PathParam("city") String city)
	{
		List<Linija> test = LinijaRedisServis.getByStation(city);
		return test;
	}
	
	@PUT
	@Path("/city")
	@Produces(MediaType.APPLICATION_JSON)
	public void recordTransit(Linija linija)
	{
		LinijaRedisServis.update(linija);
	}
	
	
	
	@POST
	@Path("/admin/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addTrain(Linija linija)
	{
		LinijaRedisServis.add(linija);
	}
	
	@GET
	@Path("/admin/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Linija> getAllAdmin()
	{
		return LinijaRedisServis.getAllAdmin();
	}
	
	@DELETE
	@Path("/admin/delete/{name}")
	public void delete(@PathParam("name") String naziv)
	{
		
		LinijaRedisServis.delete(naziv);
	}
	
	
}
