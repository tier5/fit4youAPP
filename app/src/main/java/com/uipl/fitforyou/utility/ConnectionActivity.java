package com.uipl.fitforyou.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionActivity {

	private static NetworkInfo networkInfo;

	public static boolean isNetConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		try {
			networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (networkInfo != null && networkInfo.isAvailable()
				&& networkInfo.isConnected()) {
			return true;
		}

		networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (networkInfo != null && networkInfo.isAvailable()
				&& networkInfo.isConnected()) {
			return true;
		}

		return false;
	}

}
