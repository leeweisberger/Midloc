package com.leeweisberger.midloc;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RestaurantListFragment extends Fragment {
	protected static final String MIDPOINT_KEY = "midpoint";
	protected static final String RESTAURANT_KEY = "restaurant";
	private double[] midPoint;
	private List<Restaurant> restaurantList;
	private UserInput userInput;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		setRetainInstance(true);

		userInput = (UserInput) getActivity().getIntent().getSerializableExtra(MainScreenFragment.COORDINATES_KEY);
		userInput.getCoordinates();
		midPoint = getMidPoint(userInput.getCoordinates());
		getRestaurantList();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    MenuInflater i = getActivity().getMenuInflater();
	    i.inflate(R.menu.action_bar_layout, menu);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = (View) inflater.inflate(R.layout.fragment_restaurant_list, parent, false);
		ListView lv = (ListView) v.findViewById(R.id.listview);
		lv.setDivider(null);
		lv.setDividerHeight(0);
		final ListAdapter customAdapter = new ListAdapter(getActivity(), R.layout.restaurant_list_adapter, restaurantList);
		
		lv.setAdapter(customAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Restaurant r = customAdapter.getItem(position);
				Intent i = new Intent(getActivity(),RestaurantInfoActivity.class);

				i.putExtra(RESTAURANT_KEY, r);
				i.putExtra(MainScreenFragment.COORDINATES_KEY, userInput.getCoordinates());
				startActivity(i);
				
			}
		});
		return v;
	}

	private void getRestaurantList() {
		RestaurantSearcher searcher = new RestaurantSearcher();
		try {
			restaurantList = searcher.execute(userInput).get(2,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	private double[] getMidPoint(double[] coordinates){
		double[] midPoint = new double[2];
		midPoint[0] = (coordinates[0]+coordinates[2])/2;
		midPoint[1] = (coordinates[1]+coordinates[3])/2;
		return midPoint;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	NavUtils.navigateUpFromSameTask(getActivity());
	    	return true;
	    case R.id.action_bar_map:
	    	Intent i = new Intent(getActivity(),MapActivity.class);
			Bundle bundle = new Bundle();
			bundle.putDoubleArray(MainScreenFragment.COORDINATES_KEY, userInput.getCoordinates());
			bundle.putDoubleArray(MIDPOINT_KEY,midPoint);
			i.putExtras(bundle);
			startActivity(i);
	    	
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
