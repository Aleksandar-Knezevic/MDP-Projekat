package org.unibl.etf.mdp.zsmdp.gui;

import java.util.HashMap;

public class Linija {
	String nazivLinije;
	HashMap<String, String> vozProlazakMapa;
	public String getNazivLinije() {
		return nazivLinije;
	}
	public void setNazivLinije(String nazivLinije) {
		this.nazivLinije = nazivLinije;
	}
	public HashMap<String, String> getVozProlazakMapa() {
		return vozProlazakMapa;
	}
	public void setVozProlazakMapa(HashMap<String, String> vozProlazakMapa) {
		this.vozProlazakMapa = vozProlazakMapa;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nazivLinije == null) ? 0 : nazivLinije.hashCode());
		result = prime * result + ((vozProlazakMapa == null) ? 0 : vozProlazakMapa.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Linija other = (Linija) obj;
		if (nazivLinije == null) {
			if (other.nazivLinije != null)
				return false;
		} else if (!nazivLinije.equals(other.nazivLinije))
			return false;
		if (vozProlazakMapa == null) {
			if (other.vozProlazakMapa != null)
				return false;
		} else if (!vozProlazakMapa.equals(other.vozProlazakMapa))
			return false;
		return true;
	}
	public Linija(String nazivLinije, HashMap<String, String> vozProlazakMapa) {
		super();
		this.nazivLinije = nazivLinije;
		this.vozProlazakMapa = vozProlazakMapa;
	}
	public Linija() {
		super();
	}
	public Linija(String nazivLinije) {
		super();
		this.nazivLinije = nazivLinije;
		vozProlazakMapa = new HashMap();
	}
	
	

}
