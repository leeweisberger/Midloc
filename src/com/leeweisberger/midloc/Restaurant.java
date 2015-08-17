package com.leeweisberger.midloc;

import java.io.Serializable;

import org.json.JSONObject;

public class Restaurant implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	public String getIcon() {
		return icon;
	}
	public String isOpenNow() {
		return openNow;
	}
	public int getPrice() {
		return price;
	}
	public double getRating() {
		return rating;
	}
	public double[] getCoordinates() {
		return coordinates;
	}
	private String address;
	private String icon;
	private String openNow;
	private int price=0;
	private double rating=0;
	private double[] coordinates;
	public Restaurant(String name, String address, double lat, double longitude, String icon,
			String openNow, int price, double rating, String photo) {
		this.name = name;
		this.address = address;
		this.icon = icon;
		this.openNow = openNow;
		this.price = price;
		this.rating = rating;
		this.coordinates = new double[]{lat,longitude};
		this.icon=photo;
	}

	public Restaurant (JSONObject object){
		try{
			if(object.has("name"))
				this.name = object.getString("name");
			else
				this.name="Name Unkown";
			if(object.has("opening_hours"))
				this.openNow = object.getJSONObject("opening_hours").getString("open_now");
			else
				this.openNow="unknown";
			if(object.has("vicinity"))
				this.address =object.getString("vicinity");
			else
				this.address="Address Unknown";
			if(object.has("price_level"))
				this.price = object.getInt("price_level");
			if(object.has("rating"))
				this.rating = object.getDouble("rating");
			if(object.has("photos")){
				this.icon = ((JSONObject) object.getJSONArray("photos").get(0)).getString("photo_reference");
			}
			else if(object.has("icon")){
				this.icon = object.getString("icon");
			}
			if(object.has("geometry")){
				double lat = object.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
				double longitude = object.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
				this.coordinates = new double[] {lat, longitude};
			}
			
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
	}

}
