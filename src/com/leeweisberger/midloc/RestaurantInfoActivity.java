package com.leeweisberger.midloc;

import android.support.v4.app.Fragment;

public class RestaurantInfoActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new RestaurantInfoFragment();
	}

}
