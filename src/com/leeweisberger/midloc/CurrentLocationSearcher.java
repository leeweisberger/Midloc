package com.leeweisberger.midloc;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;

public class CurrentLocationSearcher extends AsyncTask<Void, Void, double[]> {
	
	private MainScreenFragment fragment;

	public CurrentLocationSearcher(MainScreenFragment fragment){
		this.fragment = fragment;
	}

	@Override
	protected double[] doInBackground(Void... params) {
		double[] latLong = getLatLong();
		Geocoder geocoder = new Geocoder(fragment.getActivity(), Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocation(latLong[0], latLong[1], 1);
		} catch (IOException e) {
			return null;
		} 
		if(addresses.size()==0)
			return null;
		return new double[]{addresses.get(0).getLatitude(), addresses.get(0).getLongitude()};
	}
	
	@Override
	protected void onPostExecute(double[] result) {
		fragment.currentLocationCallback(result);
	}
	
	private double[] getLatLong(){
		LocationManager lm = (LocationManager)fragment.getActivity().getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		return new double[]{latitude,longitude};
	}

}
