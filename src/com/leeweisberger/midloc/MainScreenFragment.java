package com.leeweisberger.midloc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.Parse;

public class MainScreenFragment extends Fragment {
	private static final String GO_KEYBOARD = "Go";
	private static final String SEARCH_MESSAGE = "Searching for Locations...";
	private static final String ZIP_ERROR = "Please input valid US Zip Codes!";
	private static final String ERROR = "Your search could not be executed at this time";
	private static final String INTERNET_TOAST = "You are not connected to the internet";
	public static final String CURRENT_LOCATION = "Your Location";
	public static final String COORDINATES_KEY = "coordinates";
	private EditText editYourLocation;
	private ProgressDialog progress;
	private double[] yourCoordinates;
	private ToggleButton toggleCurrent;
	private Spinner spinner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		Parse.initialize(getActivity(), "OSc32QKbujg5qGORlVte2rc3klN5gWZdTkgC3bwt", "Bjc4huIY3Fvrr1kLyA9UVjAYj5ujgEn9VCWR7gsG");
		progress = new ProgressDialog(getActivity());
		progress.setTitle(SEARCH_MESSAGE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_main_screen, parent, false);
		spinner = (Spinner) v.findViewById(R.id.spinner_poi);
		editYourLocation = (EditText) v.findViewById(R.id.enter_your_location);
		final EditText editTheirLocation = (EditText) v.findViewById(R.id.enter_their_location);
		toggleCurrent = (ToggleButton) v.findViewById(R.id.toggle_current);
		final Button searchButton = (Button) v.findViewById(R.id.search_button);
		editYourLocation.setImeActionLabel(GO_KEYBOARD, KeyEvent.KEYCODE_ENTER);
		editTheirLocation.setImeActionLabel(GO_KEYBOARD, KeyEvent.KEYCODE_ENTER);
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isNetworkAvailable()){
					showToast(INTERNET_TOAST);
					return;
				}
				String theirZip = editTheirLocation.getText().toString();
				String yourZip = editYourLocation.getText().toString();
				if(!isValidZip(yourZip) || !isValidZip(theirZip)){
					showToast(ZIP_ERROR);
				}
				else{
					getCoordinates(yourZip, theirZip);
				}
			}
		});

		toggleCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					hideKeyboard();
					if(!isNetworkAvailable()){
						showToast(INTERNET_TOAST);
						toggleCurrent.setChecked(false);
						return;
					}
					CurrentLocationSearcher currentLocationSearcher = new CurrentLocationSearcher(MainScreenFragment.this);
					currentLocationSearcher.execute();
				} else{
					yourCoordinates=null;
					editYourLocation.setText("");
					editYourLocation.setKeyListener((KeyListener)editYourLocation.getTag());
					editYourLocation.setSelection(editYourLocation.getText().length());
				}
			}
		});

		editTheirLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == 0) {
					hideKeyboard();
					searchButton.performClick();
					return true;
				}
				return false;
			}
		});
		editYourLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == 0) {
					hideKeyboard();
					searchButton.performClick();
					return true;
				}
				return false;
			}
		});
		return v; 

	}

	public void coordinateSearcherCallback(double[] coordinates){
		if(coordinates==null){
			showToast(ERROR);
			return;
		}
		if(coordinates[0]==0.0 && coordinates[1]==0.0 && yourCoordinates!=null){
			coordinates[0]=yourCoordinates[0]; coordinates[1]=yourCoordinates[1];
		}
		for(double d : coordinates){
			if( d==0.0){
				showToast(ERROR);
				return;
			}
		}
			
		progress.dismiss();
		startRestaurantListActivity(coordinates);
	}

	private boolean isValidZip(String zip){
		if(zip.equals(CURRENT_LOCATION))
			return true;
		String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(zip);
		return matcher.matches();
	}
	
	private void startRestaurantListActivity(double[] coordinates) {
		Intent i = new Intent(getActivity(),RestaurantListActivity.class);
		UserInput userInput = new UserInput(coordinates, String.valueOf(spinner.getSelectedItem()));
		i.putExtra(COORDINATES_KEY, userInput);
		startActivity(i);
	}

	private void getCoordinates(String yourZip, String theirZip) {
		progress.show();
		CoordinateSearcher searcher = new CoordinateSearcher(this);
		searcher.execute(yourZip, theirZip);

	}

	private void showToast(String toastText) {
		Context context = getActivity().getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, toastText, duration);
		toast.show();
	}
	private void hideKeyboard() {   
		// Check if no view has focus:
		View view = getActivity().getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void currentLocationCallback(double[] result) {
		if(result==null){
			toggleCurrent.setChecked(false);
			return;
		}
		editYourLocation.setText(CURRENT_LOCATION);
		editYourLocation.setTag(editYourLocation.getKeyListener());
		editYourLocation.setKeyListener(null);
		editYourLocation.setSelection(editYourLocation.getText().length());
		yourCoordinates = result;
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
