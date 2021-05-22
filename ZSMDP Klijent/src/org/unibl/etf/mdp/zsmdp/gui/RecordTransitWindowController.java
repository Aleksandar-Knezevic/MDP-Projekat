package org.unibl.etf.mdp.zsmdp.gui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;

import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class RecordTransitWindowController {

	
	String station;
	
	
	@FXML
	ComboBox<String> linijeComboBox;
	
	
	
	public void init()
	{
		try
		{
			InputStream is = new URL("http://localhost:8080/CZSMDPServer/api/rest/stations/"+station).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray(jsonText);
			Gson gson = new Gson();
			List<Linija> linije = new ArrayList<Linija>();
			for(int i=0;i<json.length();i++)
				linije.add(gson.fromJson(json.get(i).toString(), Linija.class));
			
			linijeComboBox.getItems().addAll(linije.stream().map(e -> e.nazivLinije).collect(Collectors.toList()));
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	
	
	
	
	public void evidentirajButton()
	{
		String city = linijeComboBox.getValue();
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
