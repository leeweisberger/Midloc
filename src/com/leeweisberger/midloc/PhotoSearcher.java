package com.leeweisberger.midloc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class PhotoSearcher extends AsyncTask<String,Void,Bitmap> {

	@Override
	protected Bitmap doInBackground(String... params) {
		String image = params[0];
		if(image==null)
			return null;
		URL url = null;
		try {
			if(image.startsWith("http"))
				url = new URL(image);
			else
				url = new URL("https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&maxheight=300&photoreference="+image + "&key=" + "AIzaSyCVmEeZP5v1n59FxsMr71bETu0I0FrI_W4");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		//In URL pass your link

		Bitmap bmp = null;
		try {
			bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmp;
	}

}
