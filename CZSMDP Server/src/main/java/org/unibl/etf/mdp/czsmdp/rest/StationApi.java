package org.unibl.etf.mdp.czsmdp.rest;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
		Linija a = new Linija("Voz A");
			a.vozProlazakMapa.put("Banja Luka", "Nije prosao");
			a.vozProlazakMapa.put("Sarajevo", "22.05.2021 08:51");
			a.vozProlazakMapa.put("Doboj", "Nije prosao");
		Linija b = new Linija("Voz B");
			b.vozProlazakMapa.put("Prijedor", "18.04.2020 22:23");
			b.vozProlazakMapa.put("Banja Luka", "28.04.2020 04:44");
			b.vozProlazakMapa.put("Sarajevo", "Nije prosao");
		List<Linija> linije = new ArrayList<Linija>();
			linije.add(a);
			linije.add(b);
		return linije;
	}
	
	@POST
	@Path("/city")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean recordTransit(@PathParam("city") String city)
	{
		return true;
	}
	
	
	
	@POST
	@Path("/admin/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addTrain(Linija linija)
	{
		System.out.println("Nesto doslo");
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
