package org.unibl.etf.mdp.czsmdpclient.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;

import org.json.JSONArray;
import org.unibl.etf.mdp.azsmdp.rmi.AZSRMIinterface;
import org.unibl.etf.mdp.czsmdp.soap.SoapLogin;
import org.unibl.etf.mdp.czsmdp.soap.SoapLoginServiceLocator;
import org.unibl.etf.mdp.logger.MyLogger;
import org.unibl.etf.mdp.messages.MulticastAcceptThread;

import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainWindowController implements Initializable{

	public HashMap<String, Integer> locationPortMapping;
	public List<Linija> linije;
	public static String POLICY_FILE = "policyfile.txt";
	public static int REGISTRY_NO = 1099;
	public static String STUB_NAME = "AZS";
	public static String ADD_URL = "http://localhost:8080/CZSMDPServer/api/rest/stations/admin/add";
	public static String ALL_URL = "http://localhost:8080/CZSMDPServer/api/rest/stations/admin/all";
	public static String DEL_URL = "http://localhost:8080/CZSMDPServer/api/rest/stations/admin/delete/";
	
	
	@FXML
	public ComboBox<String> gradoviBox;
	@FXML
	public ComboBox<String> korisniciBox;
	@FXML
	public TextField usernameField;
	@FXML
	public TextField passwordField;
	@FXML
	public ComboBox<String> filesComboBox;
	@FXML
	public TextArea notificationArea;
	@FXML
	public TextField nazivLinijePolje;
	@FXML
	public TextField redVoznjePolje;
	@FXML
	public ComboBox<String> linijeBox;
	@FXML
	public TextArea redVoznjelabela;
	
	
	public void getUsers()
	{
		SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
		try {
		SoapLogin login = locator.getSoapLogin();
		String station = gradoviBox.getValue().trim() + "#" + locationPortMapping.get(gradoviBox.getValue())+".xml";
		String[] users = login.getUsers(station).split("#");
		korisniciBox.getItems().clear();
		for(int i=0;i<users.length;i++)
			korisniciBox.getItems().add(users[i]);
			
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	public void addUser()
	{
		getUsers();
		if(!korisniciBox.getItems().contains(usernameField.getText()))
		{
			SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
			try {
			SoapLogin login = locator.getSoapLogin();
			String station = gradoviBox.getValue().trim() + "#" + locationPortMapping.get(gradoviBox.getValue())+".xml";
			login.addUser(usernameField.getText().trim(), passwordField.getText().trim(), station);
			
			usernameField.setText("");
			passwordField.setText("");
			}
			catch (Exception e) {
				MyLogger.log(Level.WARNING, e.getMessage(), e);
			}
		}
		
	}
	
	
	public void deleteUser()
	{
		
		String station = gradoviBox.getValue().trim() + "#" + locationPortMapping.get(gradoviBox.getValue())+".xml";
		String user = korisniciBox.getValue();
		try
		{
			SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
			SoapLogin login = locator.getSoapLogin();
			login.deleteUser(user, station);
			getUsers();
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	
	
	
	public void getReports()
	{
		File file = new File(POLICY_FILE);
		System.setProperty("java.security.policy", file.getAbsolutePath());
		if(System.getSecurityManager()==null)
			System.setSecurityManager(new SecurityManager());
		try
		{
			Registry registry = LocateRegistry.getRegistry(REGISTRY_NO);
			AZSRMIinterface rmiInterface = (AZSRMIinterface) registry.lookup(STUB_NAME);
			
			filesComboBox.getItems().clear();
			String[] result = rmiInterface.listAll().split("#");
			for(int i=0;i<result.length;i++)
				filesComboBox.getItems().add(result[i]);
			
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	public void downloadReport()
	{
		try
		{
			Registry registry = LocateRegistry.getRegistry(REGISTRY_NO);
			AZSRMIinterface rmiInterface = (AZSRMIinterface) registry.lookup(STUB_NAME);
			byte[] file = rmiInterface.download(filesComboBox.getValue().split("\\.Ve")[0]);
			FileOutputStream fos = new FileOutputStream(new File(filesComboBox.getValue().split("\\.Ve")[0]));
			fos.write(file);
			fos.close();
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	
	public void addTimetable()
	{
		try
		{
			String nazivLinije = nazivLinijePolje.getText();
			String redVoznje = redVoznjePolje.getText();
			String gradoviVremena[] = redVoznje.split(",");
			HashMap<String, Vrijeme> mapa = new HashMap<String, Vrijeme>();
			for(int i=0;i<gradoviVremena.length;i++)
			{
				String[] gradVrijeme = gradoviVremena[i].split("-");
				Vrijeme v = new Vrijeme(gradVrijeme[1], "Nije prosao");
				mapa.put(gradVrijeme[0], v);
				
			}
			
			Linija linija = new Linija(nazivLinije, mapa);
			Gson gson = new Gson();
			String res = gson.toJson(linija);
			new Thread(() ->
			{
				OkHttpClient client = new OkHttpClient();
				RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), res);
				Request request = new Request.Builder()
						.url(ADD_URL)
						.post(body)
						.build();
				try {
					client.newCall(request).execute();
				} catch (IOException e) {
					MyLogger.log(Level.WARNING, e.getMessage(), e);
				}
				
			}).start();
			
			
			nazivLinijePolje.setText("");
			redVoznjePolje.setText("");
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		
	}
	
	
	
	public void dohvatiLinije()
	{
		try
		{
			if(linije==null)
				linije = new ArrayList<Linija>();
			else
				linije.clear();
			InputStream is = new URL(ALL_URL).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray(jsonText);
			Gson gson = new Gson();
			for(int i=0;i<json.length();i++)
				linije.add(gson.fromJson(json.get(i).toString(), Linija.class));
			linijeBox.getItems().clear();
			linije.forEach(e -> {linijeBox.getItems().add(e.nazivLinije);});
		}
		catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		
	}
	
	
	public void dohvatiRed()
	{
		String voz = linijeBox.getValue();
		String res = "";
		for(int i=0;i<linije.size();i++)
		{
			Linija a = linije.get(i);
			if(a.nazivLinije.equals(voz))
			{
				
				Set<String> gradVrijeme = a.vozProlazakMapa.keySet();
				for(Iterator<String> it = gradVrijeme.iterator();it.hasNext();)
				{
					String grad = it.next();
					res+=grad+"-"+"Ocekivano vrijeme: "+a.vozProlazakMapa.get(grad).ocekivaniProlazak+" Stvarni prolazak: "+a.vozProlazakMapa.get(grad).stvarniProlazak + "\n";
				}
			}
		}
		redVoznjelabela.setText(res);
	}
	
	
	public void obrisiLiniju()
	{
		String linija = linijeBox.getValue();
		redVoznjelabela.setText("");
		new Thread(() ->
		{
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.url(DEL_URL+linija)
					.delete()
					.build();
			try {
				client.newCall(request).execute();
				Platform.runLater(() -> dohvatiLinije());
			} catch (IOException e) {
				MyLogger.log(Level.WARNING, e.getMessage(), e);
			}
			
		}).start();
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		locationPortMapping = new HashMap<String, Integer>();
		SoapLoginServiceLocator locator = new SoapLoginServiceLocator();
		try {
		SoapLogin login = locator.getSoapLogin();
		String allLocations = login.getStations();
		//System.out.println(allLocations);
		String[] portLocations = allLocations.split("@");
		for(int i=0;i<portLocations.length;i++)
		{
			String[] split = portLocations[i].split("#");
			locationPortMapping.put(split[0], Integer.parseInt(split[1].split("\\.")[0]));
		}
		
		} catch (Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
		gradoviBox.getItems().addAll(locationPortMapping.keySet());
		getReports();
		new MulticastAcceptThread(this);
		dohvatiLinije();
		
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
