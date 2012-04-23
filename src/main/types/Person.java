package main.types;

public class Person {
	private Double lat;
	private Double lon;
	private int id;

	public Person(int id) {
		lat = 31.0000001d;
		lon = 31.0000001d;
		this.id = id;
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
