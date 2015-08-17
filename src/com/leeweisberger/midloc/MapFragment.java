package com.leeweisberger.midloc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapFragment extends Fragment {

	private MapView mMapView;
	private double[] coordinates;
	private double[] midPoint;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
		Bundle bundle = getActivity().getIntent().getExtras();
		coordinates = bundle.getDoubleArray(MainScreenFragment.COORDINATES_KEY);
		midPoint = bundle.getDoubleArray(RestaurantListFragment.MIDPOINT_KEY);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    MenuInflater i = getActivity().getMenuInflater();
	    i.inflate(R.menu.reset_bar_layout, menu);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflat and return the layout
		View v = inflater.inflate(R.layout.fragment_map, container,
				false);
		mMapView = (MapView) v.findViewById(R.id.mapView);
		mMapView.onCreate(savedInstanceState);

		mMapView.onResume();// needed to get the map to display immediately

		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map map = new Map();
		map.makeMap(mMapView, midPoint, coordinates, "Mid Point");
		
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	NavUtils.navigateUpFromSameTask(getActivity());
	    	return true;
	    case R.id.action_bar_reset:
	    	Intent i = new Intent(getActivity(),MainScreenActivity.class);
			startActivity(i);
	    	
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}