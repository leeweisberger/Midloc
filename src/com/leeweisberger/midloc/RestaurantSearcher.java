package com.leeweisberger.midloc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class RestaurantSearcher extends AsyncTask<UserInput, Void, List<Restaurant>> {

	private double[] getMidPoint(double[] coordinates){
		double[] midPoint = new double[2];
		midPoint[0] = (coordinates[0]+coordinates[2])/2;
		midPoint[1] = (coordinates[1]+coordinates[3])/2;
		return midPoint;
	}

	public void getMiddleRestaurants(double[] coordinates) throws IOException{
		double[] midPointCoordinates = getMidPoint(coordinates);
		System.out.println(midPointCoordinates[0]);
		String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + midPointCoordinates[0]+ "," + midPointCoordinates[1]+"&rankby=distance&types=restaurant&key=AIzaSyCVmEeZP5v1n59FxsMr71bETu0I0FrI_W4";
		System.out.println(url);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestProperty("accept", "application/json");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
	}

	@Override
	protected List<Restaurant> doInBackground(UserInput... userInput) {
		List<Restaurant> restaurantList = new ArrayList<Restaurant>();
		try {
			UserInput ui = userInput[0];
			double[] midPointCoordinates = getMidPoint(ui.getCoordinates());
			System.out.println(midPointCoordinates[0]);
			String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + midPointCoordinates[0]+ "," + midPointCoordinates[1]+"&rankby=distance&types=" + ui.getPoi() + "&key=AIzaSyCVmEeZP5v1n59FxsMr71bETu0I0FrI_W4";
			System.out.println(url);
			URL obj;

			obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestProperty("accept", "application/json");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();


			JSONObject jsonObj = new JSONObject(response.toString());
			JSONArray restaurants = jsonObj.getJSONArray("results");
			for(int i=0; i<restaurants.length(); i++){
				Restaurant r = new Restaurant(restaurants.getJSONObject(i));
				restaurantList.add(r);
			}



		}	catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e1) {
			return null;
		}
		System.out.println(restaurantList.size());
		return restaurantList;

	}
}
