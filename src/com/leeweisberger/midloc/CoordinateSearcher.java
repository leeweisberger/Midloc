package com.leeweisberger.midloc;

import java.util.List;

import android.os.AsyncTask;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class CoordinateSearcher extends AsyncTask<String, Void, double[]>{

	private static final String LONGITUDE = "longitude";
	private static final String LATITUDE = "latitude";
	private static final String ZIP = "zip";
	private MainScreenFragment fragment;
	
	public CoordinateSearcher(MainScreenFragment fragment){
		this.fragment = fragment;
	}
	
	protected double[] doInBackground(String... coordinates) {
		String yourZip = coordinates[0];
		String theirZip = coordinates[1];
		double[] yourCoordinates;
		if(yourZip.equals(MainScreenFragment.CURRENT_LOCATION))
			yourCoordinates = new double[2];
		else
			yourCoordinates = queryParse(yourZip);

		double[] theirCoordinates = queryParse(theirZip);
		if(yourCoordinates==null || theirCoordinates==null)
			return null;
		return concat(yourCoordinates, theirCoordinates);
	}
	
	protected void onPostExecute(double[] result){
			fragment.coordinateSearcherCallback(result);
	}


	private double[] queryParse(String zip) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("US");
		final double[] coordinates = new double[2];
		query.whereEqualTo(ZIP, Integer.valueOf(zip));
		List<ParseObject> zipList = null;
		try {
			zipList = query.find();
		} catch (ParseException e) {
			return null;
		}
		if(zipList.size()==0)
			return null;
		coordinates[0] = zipList.get(0).getDouble(LATITUDE);
		coordinates[1]=zipList.get(0).getDouble(LONGITUDE);

	return coordinates;
}

private double[] concat(double[] a, double[] b) {
	int aLen = a.length;
	int bLen = b.length;
	double[] c= new double[aLen+bLen];
	System.arraycopy(a, 0, c, 0, aLen);
	System.arraycopy(b, 0, c, aLen, bLen);
	return c;
}

}
