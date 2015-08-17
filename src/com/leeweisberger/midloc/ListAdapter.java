package com.leeweisberger.midloc;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<Restaurant> {
	public ListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public ListAdapter(Context context, int resource, List<Restaurant> restaurantList) {
		super(context, resource, restaurantList);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

		if (v == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.restaurant_list_adapter, null);
		}

		Restaurant items= getItem(position);


		TextView restaurantName = (TextView) v.findViewById(R.id.restaurant_name);
		TextView open = (TextView) v.findViewById(R.id.open);
		View row = v.findViewById(R.id.adaptor);
		
		restaurantName.setText(items.getName());
		open.setText(booleanToString(items.isOpenNow()));
		if(items.isOpenNow().equals("true"))
			open.setTextColor(getContext().getResources().getColor(R.color.green));
		else if(items.isOpenNow().equals("false"))
			open.setTextColor(getContext().getResources().getColor(R.color.red));
		else
			open.setTextColor(getContext().getResources().getColor(R.color.black));
		if(position%2==0)
			v.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.list_press1));
		else{
			v.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.list_press2));
		}
		restaurantName.setTextColor(getContext().getResources().getColor(R.color.black));
		
		return v;
	}


	private String booleanToString(String open){
		if(open.equals("true"))
			return "Currently Open";
		else if(open.equals("false"))
			return "Currently Closed";
		else
			return "Hours Unavailable";
	}
}