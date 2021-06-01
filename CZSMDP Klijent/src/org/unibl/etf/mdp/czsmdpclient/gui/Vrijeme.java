package org.unibl.etf.mdp.czsmdpclient.gui;

public class Vrijeme {

	public String ocekivaniProlazak;
	public String stvarniProlazak;
	public String getOcekivaniProlazak() {
		return ocekivaniProlazak;
	}
	public void setOcekivaniProlazak(String ocekivaniProlazak) {
		this.ocekivaniProlazak = ocekivaniProlazak;
	}
	public String getStvarniProlazak() {
		return stvarniProlazak;
	}
	public void setStvarniProlazak(String stvarniProlazak) {
		this.stvarniProlazak = stvarniProlazak;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ocekivaniProlazak == null) ? 0 : ocekivaniProlazak.hashCode());
		result = prime * result + ((stvarniProlazak == null) ? 0 : stvarniProlazak.hashCode());
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
		Vrijeme other = (Vrijeme) obj;
		if (ocekivaniProlazak == null) {
			if (other.ocekivaniProlazak != null)
				return false;
		} else if (!ocekivaniProlazak.equals(other.ocekivaniProlazak))
			return false;
		if (stvarniProlazak == null) {
			if (other.stvarniProlazak != null)
				return false;
		} else if (!stvarniProlazak.equals(other.stvarniProlazak))
			return false;
		return true;
	}
	public Vrijeme(String ocekivaniProlazak, String stvarniProlazak) {
		super();
		this.ocekivaniProlazak = ocekivaniProlazak;
		this.stvarniProlazak = stvarniProlazak;
	}
	public Vrijeme() {
		super();
	}
	
	
}
