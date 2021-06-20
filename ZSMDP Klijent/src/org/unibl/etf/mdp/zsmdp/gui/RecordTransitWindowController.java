package org.unibl.etf.mdp.zsmdp.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.unibl.etf.mdp.logger.MyLogger;

import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RecordTransitWindowController {

	
	String station;
	List<Linija> linije;
	public static final String STATION_URL = "http://localhost:8080/CZSMDPServer/api/rest/stations/";
	public static final String CITY_URL = "http://localhost:8080/CZSMDPServer/api/rest/stations/city";
	
	
	@FXML
	ComboBox<String> linijeComboBox;
	
	

	
	
	
	public void init()
	{
		try
		{
			InputStream is = new URL(STATION_URL+station.replace(" ", "%20")).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray(jsonText);
			Gson gson = new Gson();
			linije = new ArrayList<Linija>();
			for(int i=0;i<json.length();i++)
				linije.add(gson.fromJson(json.get(i).toString(), Linija.class));
			
			linijeComboBox.getItems().addAll(linije.stream().map(e -> e.nazivLinije).collect(Collectors.toList()));
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	
	
	
	
	public void evidentirajButton()
	{
;
		Linija linija = linije.stream().filter(e -> e.nazivLinije.equals(linijeComboBox.getValue())).collect(Collectors.toList()).get(0);
		String ocekivano = linija.vozProlazakMapa.get(station).ocekivaniProlazak;
		linija.vozProlazakMapa.put(station, new Vrijeme(ocekivano, new SimpleDateFormat("HH:mm").format(new Date())));
		Gson gson = new Gson();
		String res = gson.toJson(linija);
		new Thread(() ->
		{
			OkHttpClient client = new OkHttpClient();
			RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), res);
			Request request = new Request.Builder()
					.url(CITY_URL)
					.put(body)
					.build();
			try {
				client.newCall(request).execute();
			} catch (IOException e) {
				MyLogger.log(Level.WARNING, e.getMessage(), e);
			}
			
		}).start();
		linijeComboBox.getScene().getWindow().hide();
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
