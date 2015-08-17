package com.leeweisberger.midloc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MapActivity extends SingleFragmentActivity  {
	public static FragmentManager fragmentManager;

	@Override
	protected Fragment createFragment() {
		fragmentManager = getSupportFragmentManager();
		return new MapFragment();
	}

}
