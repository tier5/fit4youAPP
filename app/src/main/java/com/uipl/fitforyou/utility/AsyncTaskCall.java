package com.uipl.fitforyou.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncTaskCall extends AsyncTask<Void, String, String> {

	private String URL = "";
	private ArrayList<String> key, value;
	private Activity act;
	private Context context;
	private int requestCode;

	final int CONN_WAIT_TIME = 15000;
	final int CONN_DATA_WAIT_TIME = 15000;
	DialogView dialogView = new DialogView();

	private static CallbacksFromAsynctask callBack;

	public AsyncTaskCall(Activity act, Context context, int requestCode,
			String url, ArrayList<String> key, ArrayList<String> value) {
		super();
		// TODO Auto-generated constructor stub
		this.act = act;
		this.context = context;
		this.requestCode = requestCode;
		this.URL = url;
		this.key = key;
		this.value = value;
	}

	String strMessage;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected String doInBackground(Void... params) {

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, CONN_WAIT_TIME);
		HttpConnectionParams.setSoTimeout(httpParams, CONN_DATA_WAIT_TIME);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		// HttpClient httpClient = SessionControl.getHttpclient();
		String url = URL;
		HttpPost httppost = new HttpPost(url);

		try {
			List<NameValuePair> valuepair = new ArrayList<NameValuePair>();
			for (int i = 0; i < key.size(); i++) {
				valuepair.add(new BasicNameValuePair(key.get(i), value.get(i)));
			}

			httppost.setEntity(new UrlEncodedFormEntity(valuepair));

			HttpResponse httpResponse = httpClient.execute(httppost);

			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();

				if (httpEntity != null) {

					InputStream instream = httpEntity.getContent();
					strMessage = Converter.inputStreamToString(instream)
							.toString();
				}
			} else {
				return null;
			}
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			callBack.onCallbackRecieved(requestCode, 0, "");
			act.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					Toast.makeText(
							context,
							"An error occurred while trying to connect the server. Please try again.",
							500).show();
				}
			});
			AsyncTaskCall.this.cancel(true);
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			callBack.onCallbackRecieved(requestCode, 0, "");
			act.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(
							context,
							"An error occurred while trying to connect the server. Please try again.",
							500).show();
				}
			});
			AsyncTaskCall.this.cancel(true);
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			callBack.onCallbackRecieved(requestCode, 0, "");
			act.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(
							context,
							"An error occurred while trying to connect the server. Please try again.",
							500).show();
				}
			});
			AsyncTaskCall.this.cancel(true);
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			callBack.onCallbackRecieved(requestCode, 0, "");
			act.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(
							context,
							"An error occurred while trying to connect the server. Please try again.",
							500).show();
				}
			});
			AsyncTaskCall.this.cancel(true);
			return null;
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();

		}
		return strMessage;
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		super.onProgressUpdate(progress);
		System.out.println(progress[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result != null) {
			callBack.onCallbackRecieved(requestCode, 1, result);
		} else {
			callBack.onCallbackRecieved(requestCode, 2, "");
		}

	}

	public static void setCallback(CallbacksFromAsynctask callback) {
		callBack = callback;
	}

}
