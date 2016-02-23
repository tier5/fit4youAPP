package com.uipl.fitforyou.main;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uipl.fitforyou.R;
import com.uipl.fitforyou.base.GlobalVariable;
import com.uipl.fitforyou.utility.AsyncTaskCall;
import com.uipl.fitforyou.utility.CallbacksFromAsynctask;
import com.uipl.fitforyou.utility.ConnectionActivity;
import com.uipl.fitforyou.utility.Constants;
import com.uipl.fitforyou.utility.DialogView;

public class ClientAttendanceActivity extends Activity {

	ImageView iv_client, iv_trainer;

	TextView tv_clientname, tv_clientplace, tv_client_session_date,
			tv_client_session_time, tv_trainername, tv_trainerplace;

	Button btn_client_attendance;

	DialogView d;

	AsyncTaskCall callWebService;

	String startTime = "", endTime = "", curTime = "", startDate = "";

	Date stDate, CurrentDate;

	View v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_home);
		getActionBar().hide();

		initViews();

		ListenerForAsynctask();

		btn_client_attendance.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (ConnectionActivity
						.isNetConnected(ClientAttendanceActivity.this)) {

					giveAttendance();

				} else {
					d.showSingleButtonDialog(ClientAttendanceActivity.this,
							"Please enable your internet connection");
				}
			}
		});
	}

	protected void giveAttendance() {
		// TODO Auto-generated method stub
		ArrayList<String> key = new ArrayList<String>();

		key.add("id");

		ArrayList<String> value = new ArrayList<String>();

		value.add(GlobalVariable.SessionId);

		d.showCustomSpinProgress(ClientAttendanceActivity.this);

		String url = Constants.WEBSERVICE_URL+"/webservice/present";


		callWebService = new AsyncTaskCall(
				ClientAttendanceActivity.this,
				ClientAttendanceActivity.this,
				1,
				url,
				key, value);
		callWebService.execute();

		DialogView.customSpinProgress
				.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						callWebService.cancel(true);
					}
				});
	}

	private void ListenerForAsynctask() {
		// TODO Auto-generated method stub
		AsyncTaskCall.setCallback(new CallbacksFromAsynctask() {
			@Override
			public void onCallbackRecieved(int requestCode, int statuscode,
					String result) {
				// TODO Auto-generated method stub
				d.dismissCustomSpinProgress();
				if (statuscode == 1) {
					if (requestCode == 1) {
						try {
							JSONObject obj = new JSONObject(result);
							if (obj.optString("is_present").equals("1")) {
								System.out.println("service login success....");
								Toast.makeText(ClientAttendanceActivity.this,
										"Success", 500).show();
								Intent i = new Intent(
										ClientAttendanceActivity.this,
										HomeActivity.class);
								startActivity(i);
								finish();
								overridePendingTransition(
										R.anim.open_translate,
										R.anim.close_translate);
							} else {
								// if failed
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// d.showSingleButtonDialog(
									// ClientAttendanceActivity.this,
									// "Internal Error");

									d.showSingleButtonDialog(
											ClientAttendanceActivity.this,
											"Internal Error");

								}
							});
						}
					}
				}
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	private void initViews() {
		// TODO Auto-generated method stub

		Typeface teko = Typeface.createFromAsset(getAssets(), "Teko-Light.ttf");
		Typeface lato = Typeface.createFromAsset(getAssets(),
				"Lato-Regular.ttf");
		Typeface teko_bold = Typeface.createFromAsset(getAssets(),
				"Teko-Bold.ttf");

		iv_client = (ImageView) findViewById(R.id.iv_client);
		iv_trainer = (ImageView) findViewById(R.id.iv_trainer);

		tv_clientname = (TextView) findViewById(R.id.tv_clientname);
		tv_clientname.setTypeface(teko_bold);
		tv_clientplace = (TextView) findViewById(R.id.tv_clientplace);
		tv_clientplace.setTypeface(lato);
		tv_client_session_date = (TextView) findViewById(R.id.tv_client_session_date);
		tv_client_session_date.setTypeface(lato);
		tv_client_session_time = (TextView) findViewById(R.id.tv_client_session_time);
		tv_client_session_time.setTypeface(lato);
		tv_trainername = (TextView) findViewById(R.id.tv_trainername);
		tv_trainername.setTypeface(teko_bold);
		tv_trainerplace = (TextView) findViewById(R.id.tv_trainerplace);
		tv_trainerplace.setTypeface(lato);
		btn_client_attendance = (Button) findViewById(R.id.btn_client_attendance);
		btn_client_attendance.setTypeface(teko);
		d = new DialogView();

		tv_clientname
				.setText(GlobalVariable.clientPageClientName.toUpperCase());
		tv_clientplace.setText(GlobalVariable.clientPageClientPlace);
		// tv_client_session_date.setText(new SimpleDateFormat("MMM dd, yyyy")
		// .format(new Date()));
		tv_client_session_time.setText("demo");
		tv_trainername.setText(GlobalVariable.clientPageTrainerName
				.toUpperCase());

		System.out.println("photo: " + GlobalVariable.clientPageClientPhoto);

		if (GlobalVariable.clientPageClientPhoto.length() > 0) {
			System.out.println("1");
			new DownloadImageTask(iv_client)
					.execute(GlobalVariable.clientPageClientPhoto);
			iv_trainer.setBackgroundResource(R.drawable.test);

		}
		if (GlobalVariable.clientPageTrainerPhoto.length() > 0) {
			System.out.println("2");
			new DownloadImageTask(iv_trainer)
					.execute(GlobalVariable.clientPageTrainerPhoto);
			iv_client.setBackgroundResource(R.drawable.test);

		}
		if (GlobalVariable.clientPageClientPhoto.length() > 0
				&& GlobalVariable.clientPageTrainerPhoto.length() > 0) {
			System.out.println("3");
			new DownloadImageTask(iv_client)
					.execute(GlobalVariable.clientPageClientPhoto);

			new DownloadImageTask(iv_trainer)
					.execute(GlobalVariable.clientPageTrainerPhoto);

		}
		if (GlobalVariable.clientPageClientPhoto.length() == 0
				&& GlobalVariable.clientPageTrainerPhoto.length() == 0) {
			System.out.println("4");
			iv_trainer.setBackgroundResource(R.drawable.test);
			iv_client.setBackgroundResource(R.drawable.test);
		}
		// managing start and end time
		if (GlobalVariable.startTime.equals("")
				&& GlobalVariable.endTime.equals("")) {
			tv_client_session_time.setText("No active session");
			tv_client_session_date.setText("No active session");

			btn_client_attendance.setAlpha(0.3f);
			btn_client_attendance.setText("NO SESSION");
			btn_client_attendance.setEnabled(false);
		} else {

			String st_time_text = GlobalVariable.startTime.substring(10, 17);
			System.out.println("st time text: " + st_time_text);

			String st_date = GlobalVariable.startTime.substring(0, 10);
			System.out.println("st_date : " + st_date);
			// getting as 2015-07-03

			String cdate = new SimpleDateFormat("MMM dd, yyyy")
					.format(new Date());
			CurrentDate = new Date(); // get the current date

			SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");
			SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormat4 = new SimpleDateFormat("MMM dd,yyyy");
			try {
				Date date = dateFormat.parse(st_time_text);

				startTime = dateFormat2.format(date);
				Log.e("Time", startTime);

				// formatting the start date now
				stDate = dateFormat3.parse(st_date);
				startDate = dateFormat4.format(stDate);

			} catch (ParseException e) {
			}

			tv_client_session_date.setText(startDate);

			String end_time_text = GlobalVariable.endTime.substring(10, 17);
			System.out.println("end time text: " + end_time_text);

			SimpleDateFormat dtFormat = new SimpleDateFormat("kk:mm");
			SimpleDateFormat dtFormat2 = new SimpleDateFormat("hh:mm aa");
			try {
				Date date = dtFormat.parse(end_time_text);

				endTime = dtFormat2.format(date);
				Log.e("Time end", endTime);
			} catch (ParseException e) {
			}

			// set the session time
			tv_client_session_time.setText(startTime + "-" + endTime);
		}

		// ----
		// not needed now because webservice itself will handle it
		// curTime = new SimpleDateFormat("hh:mm aa").format(new Date());
		// checkTimeDifference(startTime, 15, -15);

		// ----

		System.out.println("GlobalVariable.clientPageClientAttendance"
				+ GlobalVariable.clientPageClientAttendance);

		if (GlobalVariable.ClientPresentButtonStatus.equals("0")) {

			btn_client_attendance.setAlpha(0.3f);
			btn_client_attendance.setText("NO SESSION");
			btn_client_attendance.setEnabled(false);

		} else if (GlobalVariable.ClientPresentButtonStatus.equals("1")) {

			btn_client_attendance.setText("CONFIRM SEESION");
			btn_client_attendance.setEnabled(true);

		} else if (GlobalVariable.ClientPresentButtonStatus.equals("2")) {

			btn_client_attendance.setAlpha(0.3f);
			btn_client_attendance.setText("SESSION CONFIRMED");
			btn_client_attendance.setEnabled(false);

		} else if (GlobalVariable.ClientPresentButtonStatus.equals("3")) {

			btn_client_attendance.setAlpha(0.3f);
			btn_client_attendance.setText("SESSION MISSED");
			btn_client_attendance.setEnabled(false);
		} else if (GlobalVariable.ClientPresentButtonStatus.equals("")) {

			btn_client_attendance.setAlpha(0.3f);
			btn_client_attendance.setText("NO SESSION");
			btn_client_attendance.setEnabled(false);
		}

	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	@SuppressLint("SimpleDateFormat")
	private int checkTimeDifference(String strt_time, int x, int y) {

		// first check if start time is on the same current date

		if (stDate.after(CurrentDate)) {
			// when both dates are equal means start date and current date are
			// equal

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm");

			final Calendar c = Calendar.getInstance();

			c.add(Calendar.MINUTE, x);
			final int hour = c.get(Calendar.HOUR_OF_DAY);
			final int min = c.get(Calendar.MINUTE);

			String DateAfterXMins = (hour + ":" + min);
			Date CurrentDateAfterXMins = new Date();
			Date SessionStartTime = new Date();

			try {
				CurrentDateAfterXMins = simpleDateFormat.parse(DateAfterXMins);
				SessionStartTime = simpleDateFormat.parse(strt_time);
				System.out.println("cdax" + CurrentDateAfterXMins.toString());
				System.out.println("sst" + SessionStartTime.toString());

			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			final Calendar c1 = Calendar.getInstance();

			c1.add(Calendar.MINUTE, y);
			final int hour1 = c1.get(Calendar.HOUR_OF_DAY);
			final int min1 = c1.get(Calendar.MINUTE);

			String DateBeforeYMins = (hour1 + ":" + min1);
			Date CurrentDateBeforeYMins = new Date();

			try {
				CurrentDateBeforeYMins = simpleDateFormat
						.parse(DateBeforeYMins);
				System.out.println("cdby" + CurrentDateBeforeYMins.toString());

			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// check if session start time is before x minutes after current
			// time
			// check if session start time is after y minutes before current
			// time
			if (SessionStartTime.before(CurrentDateAfterXMins)
					&& SessionStartTime.after(CurrentDateBeforeYMins)) {
				System.out.println("valid time");
			} else {
				System.out.println("invalid time");
			}

			btn_client_attendance.setText("st dt aftr cd");

		}
		return 0;

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(ClientAttendanceActivity.this,
				ClientLoginActivity.class);
		startActivity(i);
		finish();
		overridePendingTransition(R.anim.open_scale, R.anim.close_translate);
	}

}
