package org.unibl.etf.mdp.zsmdp.gui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;

import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class TimetableWindowController {

	String station;
	List<Linija> linije;
	
	
	@FXML
	VBox timetableBox;
	
	
	
	
	
	
	
	
	public void init() {
		try
		{
		InputStream is = new URL("http://localhost:8080/CZSMDPServer/api/rest/stations/"+station.replace(" ", "%20")).openStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String jsonText = readAll(rd);
		JSONArray json = new JSONArray(jsonText);
		Gson gson = new Gson();
		linije = new ArrayList<Linija>();
		for(int i=0;i<json.length();i++)
			linije.add(gson.fromJson(json.get(i).toString(), Linija.class));

		
		
		
		
		
		
		
		
		linije.forEach(e ->
		{
			VBox one = new VBox(10);
			one.setAlignment(Pos.TOP_CENTER);
			String title = e.nazivLinije + ": ";
			Set<String> gradovi = e.vozProlazakMapa.keySet();
			for(Iterator<String> it = gradovi.iterator();it.hasNext();)
			{
				title += it.next()+"-";
			}
			String subTitle = title.substring(0, title.lastIndexOf('-'));
			Label titleLabel = new Label(subTitle);
			one.getChildren().add(titleLabel);
			for(Iterator<String> it = gradovi.iterator();it.hasNext();)
			{
				String key = it.next();
				Label label = new Label(key + "-" + e.vozProlazakMapa.get(key));
				one.getChildren().add(label);
			}
			
			timetableBox.getChildren().add(one);
			timetableBox.getChildren().add(new Label());
			
		});
		
		
		
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}
	
	
	private String readAll(Reader rd) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

}
