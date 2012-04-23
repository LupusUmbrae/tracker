package main.types;

public class Person {
	private Double lat;
	private Double lon;
	private int id;
	private Double vol;

	public Person(int id, Double vol) {
		lat = 31.0000001d;
		lon = 31.0000001d;
		this.id = id;
		this.vol = vol;
	}
	
	public double getVol() {
		return vol;
	}
	
	public void setVol(double vol){
		this.vol = vol;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public int getId() {
		return id;
	}
}
