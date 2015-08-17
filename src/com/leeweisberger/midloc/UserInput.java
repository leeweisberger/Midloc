package com.leeweisberger.midloc;

import java.io.Serializable;

public class UserInput implements Serializable{
	private static final long serialVersionUID = 1L;
	private double[] coordinates;
	private String poi;
	public UserInput(double[] coordinates, String poi) {
		this.coordinates = coordinates;
		for(PointsOfInterest p : PointsOfInterest.values()){
			if(p.getViewPOI().equals(poi))
				this.poi = p.getApiPOI();
		}
	}
	public double[] getCoordinates() {
		return coordinates;
	}
	public String getPoi() {
		if(poi==null)
			return PointsOfInterest.RESTAURANTS.getApiPOI();
		return poi;
	}
	

}
