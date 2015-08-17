package com.leeweisberger.midloc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

public class RestaurantInfoFragment extends Fragment {
	private Restaurant restaurant;
	private MapView mMapView;
	private double[] coordinates;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
		restaurant = (Restaurant) getActivity().getIntent().getSerializableExtra(RestaurantListFragment.RESTAURANT_KEY);
		coordinates = getActivity().getIntent().getDoubleArrayExtra(MainScreenFragment.COORDINATES_KEY);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    MenuInflater i = getActivity().getMenuInflater();
	    i.inflate(R.menu.reset_bar_layout, menu);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = (View) inflater.inflate(R.layout.fragment_restaurant_info, parent, false);
		mMapView = (MapView) v.findViewById(R.id.mapView);
		mMapView.onCreate(savedInstanceState);

		mMapView.onResume();// needed to get the map to display immediately

		Map map = new Map();
		map.makeMap(mMapView, restaurant.getCoordinates(), coordinates, restaurant.getName() );
		
		Bitmap bmp = null;
		try {
			bmp = new PhotoSearcher().execute(restaurant.getIcon()).get(2,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		if(bmp!=null)
			((ImageView) v.findViewById(R.id.image)).setImageBitmap(Bitmap.createScaledBitmap(bmp, 300, 300, false));
		((TextView) v.findViewById(R.id.restaurant_name)).setText(restaurant.getName());
		((TextView) v.findViewById(R.id.restaurant_address)).setText(restaurant.getAddress());
		
		RatingBar priceRatingBar = ((RatingBar) v.findViewById(R.id.price));
		priceRatingBar.setRating((float) restaurant.getPrice());
		LayerDrawable stars = (LayerDrawable) priceRatingBar.getProgressDrawable();
		stars.getDrawable(2).setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
		
		RatingBar ratingRatingBar = ((RatingBar) v.findViewById(R.id.rating));
		ratingRatingBar.setRating((float) restaurant.getRating());
		LayerDrawable stars2 = (LayerDrawable) ratingRatingBar.getProgressDrawable();
		stars2.getDrawable(2).setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);

		((Button) v.findViewById(R.id.midloc_it)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String uri = "http://maps.google.co.in/maps?q=" + restaurant.getAddress();				
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				getActivity().startActivity(intent);
				
			}
		});

		return v;
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
