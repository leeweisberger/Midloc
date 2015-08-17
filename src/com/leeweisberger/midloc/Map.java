package com.leeweisberger.midloc;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map {

	public void makeMap(MapView mapView, double[] midPoint, double[] coordinates, String midPointLabel){
		

		final GoogleMap googleMap = mapView.getMap();

		// create markers
		MarkerOptions midPointMarker = new MarkerOptions().position(
				new LatLng(midPoint[0], midPoint[1])).title(midPointLabel);
		MarkerOptions yourMarker = new MarkerOptions().position(
				new LatLng(coordinates[0],coordinates[1])).title("Your Location");
		MarkerOptions theirMarker = new MarkerOptions().position(
				new LatLng(coordinates[2], coordinates[3])).title("Their Location");
		// Changing marker icon
		midPointMarker.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

		// adding markers
		googleMap.addMarker(midPointMarker);
		googleMap.addMarker(yourMarker);
		googleMap.addMarker(theirMarker);
		
		final LatLngBounds.Builder builder = new LatLngBounds.Builder();
		
		    builder.include(midPointMarker.getPosition());
		    builder.include(yourMarker.getPosition());
		    builder.include(theirMarker.getPosition());

		googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() { 
			@Override 
			public void onMapLoaded() { 
				googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 150));
			 } 
			});
	}
}
